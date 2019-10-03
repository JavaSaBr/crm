package com.ss.jcrm.user.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncLazyConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.api.impl.DefaultUserGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public class JAsyncUserGroupDao extends AbstractJAsyncDao<UserGroup> implements UserGroupDao {

    private static final String Q_SELECT_ALL_BY_ORG_ID = "select \"id\", \"name\", \"organization_id\", \"roles\"," +
        " \"version\" from \"${schema}\".\"user_group\" where \"organization_id\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"organization_id\", \"roles\"," +
        " \"version\" from \"${schema}\".\"user_group\" where \"id\" = ?";

    private static final String Q_INSERT = "insert into \"${schema}\".\"user_group\" (\"name\", \"organization_id\")" +
        " values (?, ?) RETURNING id";

    private final String querySelectById;
    private final String querySelectAllByOrgId;
    private final String queryInsert;

    private final OrganizationDao organizationDao;

    public JAsyncUserGroupDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull OrganizationDao organizationDao
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectAllByOrgId = Q_SELECT_ALL_BY_ORG_ID.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.organizationDao = organizationDao;
    }

    @Override
    protected @NotNull Class<UserGroup> getEntityType() {
        return UserGroup.class;
    }

    @Override
    public @NotNull Mono<@NotNull UserGroup> create(@NotNull String name, @NotNull Organization organization) {
        return insert(
            queryInsert,
            List.of(name, organization.getId()),
            id -> new DefaultUserGroup(id, name, organization)
        );
    }

    @Override
    public @NotNull Mono<@Nullable UserGroup> findById(long id) {
        return selectAsync(querySelectById, id, converter());
    }

    @Override
    public @NotNull Mono<@NotNull Set<UserGroup>> findAll(@NotNull Organization organization) {
        return selectAllAsync(querySelectAllByOrgId, organization.getId(), converter())
            .map(Set::copyOf);
    }

    private @NotNull JAsyncLazyConverter<@NotNull JAsyncUserGroupDao, @NotNull UserGroup> converter() {
        return JAsyncUserGroupDao::toUserGroup;
    }

    private @NotNull Mono<@NotNull UserGroup> toUserGroup(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var version = notNull(data.getInt(4));
        var name = data.getString(1);
        var orgId = notNull(data.getLong(2));
        var roles = JAsyncUtils.fromJsonIds(
            data.getString(3),
            AccessRole::require
        );

        return organizationDao.requireById(orgId)
            .map(organization -> new DefaultUserGroup(
                id, name,
                organization,
                roles, version
            ));
    }
}
