package com.ss.jcrm.user.jasync.dao;

import static com.ss.jcrm.jasync.util.JAsyncUtils.toDateTime;
import static com.ss.jcrm.jasync.util.JAsyncUtils.toJavaInstant;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.user.api.EmailConfirmation;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.jcrm.user.api.impl.DefaultEmailConfirmation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

public class JAsyncEmailConfirmationDao extends AbstractJAsyncDao<EmailConfirmation> implements EmailConfirmationDao {

    private static final String FIELD_LIST = "\"id\", \"code\", \"email\", \"expiration\"";

    private static final String Q_SELECT_BY_ID = "select " + FIELD_LIST +
        " from \"${schema}\".\"email_confirmation\" where \"id\" = ?";
    private static final String Q_SELECT_BY_CODE_AND_EMAIL = "select " + FIELD_LIST +
        " from \"${schema}\".\"email_confirmation\" where \"code\" = ? and \"email\" = ?";

    private static final String Q_INSERT = "insert into \"${schema}\".\"email_confirmation\"" +
        " (\"code\", \"email\", \"expiration\") values (?, ?, ?) returning id";

    private static final String Q_DELETE_BY_ID = "delete from \"${schema}\".\"email_confirmation\" where \"id\" = ?";

    private final String querySelectById;
    private final String querySelectByCodeAndEmail;
    private final String queryInsert;
    private final String queryDeleteById;

    public JAsyncEmailConfirmationDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectByCodeAndEmail = Q_SELECT_BY_CODE_AND_EMAIL.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.queryDeleteById = Q_DELETE_BY_ID.replace("${schema}", schema);
    }

    @Override
    protected @NotNull Class<EmailConfirmation> getEntityType() {
        return EmailConfirmation.class;
    }

    @Override
    public @NotNull Mono<@NotNull EmailConfirmation> create(
        @NotNull String code,
        @NotNull String email,
        @NotNull Instant expiration
    ) {
        return insert(
            queryInsert,
            List.of(code, email, toDateTime(expiration)),
            id -> new DefaultEmailConfirmation(id, code, email, expiration)
        );
    }

    @Override
    public @NotNull Mono<@Nullable EmailConfirmation> findById(long id) {
        return select(querySelectById, id, converter());
    }

    @Override
    public @NotNull Mono<@Nullable EmailConfirmation> findByEmailAndCode(@NotNull String email, @NotNull String code) {
        return select(querySelectByCodeAndEmail, List.of(code, email), converter());
    }

    private @NotNull JAsyncConverter<@NotNull JAsyncEmailConfirmationDao, @NotNull EmailConfirmation> converter() {
        return JAsyncEmailConfirmationDao::toEmailConformation;
    }

    @Override
    public @NotNull Mono<Boolean> delete(long id) {
        return delete(queryDeleteById, id);
    }

    private @NotNull EmailConfirmation toEmailConformation(@NotNull RowData data) {
        return new DefaultEmailConfirmation(
            notNull(data.getLong(0)),               // id
            data.getString(1),                      // code
            data.getString(2),                      // email
            toJavaInstant(notNull(data.getDate(3))) // expiration
        );
    }
}
