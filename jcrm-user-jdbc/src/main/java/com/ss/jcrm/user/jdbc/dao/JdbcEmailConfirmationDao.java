package com.ss.jcrm.user.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.jdbc.dao.AbstractJdbcDao;
import com.ss.jcrm.user.api.EmailConfirmation;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.jcrm.user.jdbc.JdbcEmailConfirmation;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JdbcEmailConfirmationDao extends AbstractJdbcDao<EmailConfirmation> implements EmailConfirmationDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"code\", \"email\", \"expiration\"" +
        " from \"email_confirmation\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_CODE_AND_EMAIL = "select \"id\", \"code\", \"email\", \"expiration\"" +
        " from \"email_confirmation\" where \"code\" = ? and \"email\" = ?";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"email_confirmation\" (\"code\", \"email\", \"expiration\")" +
        " values (?, ?, ?)";

    @Language("PostgreSQL")
    private static final String Q_DELETE_BY_ID = "delete from \"email_confirmation\" where \"id\" = ?";

    public JdbcEmailConfirmationDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull EmailConfirmation create(@NotNull String code, @NotNull String email, @NotNull Instant expiration) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, code);
            statement.setString(2, email);
            statement.setTimestamp(3, Timestamp.from(expiration));
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcEmailConfirmation(rs.getLong(1), code, email, expiration);
                } else {
                    throw new IllegalStateException(
                        "Can't receive generated id for the new email conformation [" + code + "-" + email + "]."
                    );
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull EmailConfirmation> createAsync(
        @NotNull String code,
        @NotNull String email,
        @NotNull Instant expiration
    ) {
        return supplyAsync(() -> create(code, email, expiration), fastDbTaskExecutor);
    }

    @Override
    public @Nullable EmailConfirmation findByEmailAndCode(@NotNull String email, @NotNull String code) {
        return findByStringString(
            Q_SELECT_BY_CODE_AND_EMAIL,
            code,
            email,
            JdbcEmailConfirmationDao::toEmailConformation
        );
    }

    @Override
    public @NotNull CompletableFuture<@Nullable EmailConfirmation> findByEmailAndCodeAsync(
        @NotNull String email,
        @NotNull String code
    ) {
        return supplyAsync(() -> findByEmailAndCode(email, code), fastDbTaskExecutor);
    }

    @Override
    public boolean delete(long id) {
        return deleteByLong(Q_DELETE_BY_ID, id);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Boolean> deleteAsync(long id) {
        return supplyAsync(() -> delete(id), fastDbTaskExecutor);
    }

    @Override
    public @Nullable EmailConfirmation findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcEmailConfirmationDao::toEmailConformation);
    }

    private @NotNull EmailConfirmation toEmailConformation(@NotNull ResultSet rs) throws SQLException {
        return new JdbcEmailConfirmation(
            rs.getLong(1),                  // id
            rs.getString(2),                // code
            rs.getString(3),                // email
            rs.getTimestamp(4).toInstant()  // expiration
        );
    }
}
