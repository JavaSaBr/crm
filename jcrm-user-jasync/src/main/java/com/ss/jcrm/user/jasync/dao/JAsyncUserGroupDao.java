package com.ss.jcrm.user.jasync.dao;

import static com.ss.jcrm.base.utils.DateUtils.toOffsetDateTime;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.base.utils.DateUtils;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncLazyConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.api.impl.DefaultUserGroup;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.Array;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncUserGroupDao extends AbstractJAsyncDao<UserGroup> implements UserGroupDao {

    private static final String FIELD_LIST = """
        "id", "name", "organization_id", "roles", "modified", "created", "version"
        """;

    private static final String Q_INSERT = """
        insert into "${schema}"."user_group" ("name", "organization_id", "roles", "modified", "created") 
        values (?, ?, ?, ?, ?) returning id
        """;

    private static final String Q_SELECT_ALL_BY_ORG = """
        select ${field-list} from "${schema}"."user_group" where "organization_id" = ?
        """;

    private static final String Q_SELECT_BY_ID = """
        select ${field-list} from "${schema}"."user_group" where "id" = ?
        """;

    private static final String Q_SELECT_BY_ID_AND_ORG = """
        select ${field-list} from "${schema}"."user_group" where "id" = ? and "organization_id" = ?
        """;

    private static final String Q_SELECT_BY_IDS_AND_ORG = """
        select ${field-list} from "${schema}"."user_group" where "id" in (${id-list}) AND "organization_id" = ?
        """;

    private static final String Q_COUNT_BY_NAME_AND_ORG = """
        select count("id") from "${schema}"."user_group" where "name" = ? and "organization_id" = ?
        """;

    private static final String Q_COUNT_BY_ORG = """
        select count("id") from "${schema}"."user_group" where "organization_id" = ?
        """;

    private static final String Q_SEARCH_BY_NAME_AND_ORG = """
        select ${field-list} from "${schema}"."user_group" where "name" ilike (?) AND "organization_id" = ?
        """;

    private static final String Q_UPDATE = """
        update "${schema}"."user_group" set "name" = ?, "roles" = ?, "version" = ?, "modified" = ? where "id" = ? and "version" = ?
        """;

    private static final String Q_SELECT_PAGE_BY_ORG = """
        select ${field-list} from "${schema}"."user_group" where "organization_id" = ? order by "id" limit ? offset ?
        """;

    @NotNull String querySelectById;
    @NotNull String querySelectByIdAndOrg;
    @NotNull String querySelectByIdsAndOrg;
    @NotNull String querySelectAllByOrg;
    @NotNull String queryInsert;
    @NotNull String queryCountByNameAndOrg;
    @NotNull String queryCountByOrg;
    @NotNull String querySearchByNameAndOrg;
    @NotNull String queryPageByOrg;
    @NotNull String queryUpdate;

    @NotNull OrganizationDao organizationDao;

    public JAsyncUserGroupDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull OrganizationDao organizationDao
    ) {
        super(connectionPool, schema, FIELD_LIST);
        this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
        this.querySelectByIdAndOrg = prepareQuery(Q_SELECT_BY_ID_AND_ORG);
        this.querySelectByIdsAndOrg = prepareQuery(Q_SELECT_BY_IDS_AND_ORG);
        this.querySelectAllByOrg = prepareQuery(Q_SELECT_ALL_BY_ORG);
        this.queryInsert = prepareQuery(Q_INSERT);
        this.queryCountByNameAndOrg = prepareQuery(Q_COUNT_BY_NAME_AND_ORG);
        this.queryCountByOrg = prepareQuery(Q_COUNT_BY_ORG);
        this.querySearchByNameAndOrg = prepareQuery(Q_SEARCH_BY_NAME_AND_ORG);
        this.queryPageByOrg = prepareQuery(Q_SELECT_PAGE_BY_ORG);
        this.queryUpdate = prepareQuery(Q_UPDATE);
        this.organizationDao = organizationDao;
    }

    @Override
    protected @NotNull Class<UserGroup> getEntityType() {
        return UserGroup.class;
    }

    @Override
    public @NotNull Mono<@NotNull UserGroup> create(
        @NotNull String name,
        @NotNull Set<AccessRole> roles,
        @NotNull Organization organization
    ) {

        var created = Instant.now(Clock.systemUTC());
        var createdDateTime = toOffsetDateTime(created);

        return insert(
            queryInsert,
            Arrays.asList(
                name,
                organization.getId(),
                JAsyncUtils.idsToJson(roles),
                createdDateTime,
                createdDateTime
            ),
            id -> new DefaultUserGroup(id, name, roles, organization, created, created, 0)
        );
    }

    @Override
    public @NotNull Mono<@NotNull UserGroup> update(@NotNull UserGroup userGroup) {

        userGroup.setModified(Instant.now());

        return update(
            queryUpdate,
            Arrays.asList(
                StringUtils.emptyIfNull(userGroup.getName()),
                JAsyncUtils.idsToJson(userGroup.getRoles()),
                userGroup.getVersion() + 1,
                toOffsetDateTime(userGroup.getModified()),
                userGroup.getId(),
                userGroup.getVersion()
            ),
            userGroup
        );
    }

    @Override
    public @NotNull Mono<@Nullable UserGroup> findById(long id) {
        return selectAsync(querySelectById, id, converter());
    }

    @Override
    public @NotNull Mono<@NotNull Set<UserGroup>> findAll(@NotNull Organization organization) {
        return selectAllAsync(querySelectAllByOrg, organization.getId(), converter())
            .map(Set::copyOf);
    }

    @Override
    public @NotNull Mono<Boolean> existByName(@NotNull String name, long orgId) {
        return count(queryCountByNameAndOrg, List.of(name, orgId))
            .map(count -> count > 0);
    }

    @Override
    public @NotNull Mono<@NotNull Array<UserGroup>> searchByName(@NotNull String name, long orgId) {
        var pattern = "%" + name + "%";
        return selectAllAsync(
            UserGroup.class,
            querySearchByNameAndOrg,
            List.of(pattern, orgId),
            converter()
        );
    }

    @Override
    public @NotNull Mono<@NotNull EntityPage<UserGroup>> findPageByOrg(long offset, long size, long orgId) {
        return selectAllAsync(queryPageByOrg, List.of(orgId, size, offset), converter())
            .zipWith(count(queryCountByOrg, orgId), EntityPage::new);
    }

    @Override
    public @NotNull Mono<@NotNull UserGroup> findByIdAndOrgId(long id, long orgId) {
        return selectAsync(querySelectByIdAndOrg, List.of(id, orgId), converter());
    }

    @Override
    public @NotNull Mono<@NotNull Array<UserGroup>> findByIdsAndOrgId(@Nullable long[] ids, long orgId) {

        if (ids == null || ids.length == 0) {
            return Mono.just(Array.empty());
        } else if (ids.length == 1) {
            return selectAllAsync(UserGroup.class, querySelectByIdAndOrg, List.of(ids[0], orgId), converter());
        }

        var args = new ArrayList<>(ids.length + 1);

        for (var id : ids) {
            args.add(id);
        }

        args.add(orgId);

        var query = injectIdList(querySelectByIdsAndOrg, ids);

        return selectAllAsync(UserGroup.class, query, args, converter());
    }

    private @NotNull JAsyncLazyConverter<@NotNull JAsyncUserGroupDao, @NotNull UserGroup> converter() {
        return JAsyncUserGroupDao::toUserGroup;
    }

    private @NotNull Mono<@NotNull UserGroup> toUserGroup(@NotNull RowData data) {

        // "id", "name", "organization_id", "roles", "modified", "created", "version"
        var id = notNull(data.getLong(0));
        var name = notNull(data.getString(1));
        var orgId = notNull(data.getLong(2));
        var roles = JAsyncUtils.fromJsonIds(
            data.getString(3),
            AccessRole::require
        );

        var modified = DateUtils.toUtcInstant(data.getAs(4));
        var created = DateUtils.toUtcInstant(data.getAs(5));
        var version = notNull(data.getInt(6));

        return organizationDao.requireById(orgId)
            .map(organization -> new DefaultUserGroup(
                id,
                name,
                roles,
                organization,
                created,
                modified,
                version
            ));
    }
}
