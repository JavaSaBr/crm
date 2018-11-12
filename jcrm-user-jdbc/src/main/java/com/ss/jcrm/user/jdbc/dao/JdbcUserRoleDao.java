package com.ss.jcrm.user.jdbc.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.jdbc.dao.AbstractJdbcDao;
import com.ss.jcrm.jdbc.exception.JdbcException;
import com.ss.jcrm.user.api.UserRole;
import com.ss.jcrm.user.api.dao.UserRoleDao;
import com.ss.jcrm.user.jdbc.JdbcUserRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JdbcUserRoleDao extends AbstractJdbcDao implements UserRoleDao {

    private static final String Q_SELECT_ALL = "select \"id\", \"name\" FROM \"user_role\"";
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\" FROM \"user_role\" where \"name\" = ?";
    private static final String Q_SELECT_BY_ID = "select \"name\" FROM \"user_role\" where \"id\" = ?";
    private static final String Q_INSERT = "INSERT INTO \"user_role\" (\"id\", \"name\") VALUES (DEFAULT, ?)";

    public JdbcUserRoleDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull UserRole create(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcUserRole(name, rs.getLong(1));
                } else {
                    throw new IllegalStateException("Can't receive generated id.");
                }
            }

        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull UserRole> createAsync(@NotNull String name) {
        return supplyAsync(() -> create(name), fastDbTaskExecutor);
    }

    @Override
    public @Nullable UserRole findByName(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_NAME)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcUserRole(
                        rs.getString(2),
                        rs.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new JdbcException(e);
        }

        return null;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable UserRole> findByNameAsync(@NotNull String name) {
        return supplyAsync(() -> findByName(name), fastDbTaskExecutor);
    }

    @Override
    public @Nullable UserRole findById(long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_ID)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcUserRole(rs.getString(2), id);
                }
            }

        } catch (SQLException e) {
            throw new JdbcException(e);
        }

        return null;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable UserRole> findByIdAsync(long id) {
        return supplyAsync(() -> findById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull UserRole requireById(long id) {
        return notNull(findById(id));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull UserRole> requireByIdAsync(long id) {
        return supplyAsync(() -> requireById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull Set<UserRole> getAll() {

        var result = new HashSet<UserRole>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_ALL)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new JdbcUserRole(
                        rs.getString(2),
                        rs.getLong(1))
                    );
                }
            }

        } catch (SQLException e) {
            throw new JdbcException(e);
        }

        return result;
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<UserRole>> getAllAsync() {
        return supplyAsync(this::getAll, slowDbTaskExecutor);
    }
}
