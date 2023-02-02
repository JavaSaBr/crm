package com.ss.jcrm.user.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.ifNull;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import crm.user.api.MinimalUser;
import crm.user.api.dao.MinimalUserDao;
import crm.user.api.impl.DefaultMinimalUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncMinimalUserDao extends AbstractJAsyncDao<MinimalUser> implements MinimalUserDao {

    private static final String FIELD_LIST = """
        "id", "organization_id", "email", "roles"
        """;

    private static final String Q_SELECT_BY_ID = """
        select ${field-list} from "${schema}"."user" where "id" = ?
        """;

    private static final String Q_SELECT_PAGE_BY_ORG_AND_GROUP = """
        select ${field-list} from "${schema}"."user" 
        where "organization_id" = ? and "groups" is not null and "groups" @> to_jsonb(?) order by "id" limit ? offset ?
        """;

    private static final String Q_COUNT_BY_ORG_AND_GROUP = """
        select count("id") from "${schema}"."user" 
        where "organization_id" = ? and "groups" is not null and "groups" @> to_jsonb(?)
        """;

    @NotNull String querySelectById;
    @NotNull String querySelectPageByOrgAndGroup;
    @NotNull String queryCountByOrgAndGroup;

    public JAsyncMinimalUserDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool, schema, FIELD_LIST);
        this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
        this.querySelectPageByOrgAndGroup = prepareQuery(Q_SELECT_PAGE_BY_ORG_AND_GROUP);
        this.queryCountByOrgAndGroup = prepareQuery(Q_COUNT_BY_ORG_AND_GROUP);
    }

    @Override
    protected @NotNull Class<MinimalUser> getEntityType() {
        return MinimalUser.class;
    }

    @Override
    public @NotNull Mono<@NotNull MinimalUser> findById(long id) {
        return select(querySelectById, id, converter());
    }

    private @NotNull JAsyncConverter<@NotNull JAsyncMinimalUserDao, @NotNull MinimalUser> converter() {
        return JAsyncMinimalUserDao::toMinimalUser;
    }

    @Override
    public @NotNull Mono<@NotNull EntityPage<MinimalUser>> findPageByOrgAndGroup(
        long offset,
        long size,
        long orgId,
        long groupId
    ) {
        return selectAll(querySelectPageByOrgAndGroup, List.of(orgId, groupId, size, offset), converter())
            .zipWith(count(queryCountByOrgAndGroup, List.of(orgId, groupId)), EntityPage::new);
    }

    private @NotNull MinimalUser toMinimalUser(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var orgId = ifNull(data.getLong(1), 0L);
        var email = data.getString(2);

        var roles = JAsyncUtils.fromJsonIds(
            data.getString(3),
            AccessRole::require
        );

        return new DefaultMinimalUser(id, orgId, email, roles);
    }
}
