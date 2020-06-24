package com.ss.jcrm.user.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.ifNull;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.MinimalUser;
import com.ss.jcrm.user.api.dao.MinimalUserDao;
import com.ss.jcrm.user.api.impl.DefaultMinimalUser;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@Log4j2
public class JAsyncMinimalUserDao extends AbstractJAsyncDao<MinimalUser> implements MinimalUserDao {

    private static final String FIELD_LIST = "\"id\", \"organization_id\", \"email\", \"roles\"";

    private static final String Q_SELECT_BY_ID = "select " + FIELD_LIST +
        " from \"${schema}\".\"user\" where \"id\" = ?";

    private final String querySelectById;

    public JAsyncMinimalUserDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
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
