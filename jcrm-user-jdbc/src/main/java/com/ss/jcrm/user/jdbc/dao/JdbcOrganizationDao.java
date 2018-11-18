package com.ss.jcrm.user.jdbc.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.jdbc.dao.AbstractNamedObjectJdbcDao;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.jdbc.JdbcOrganization;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Log4j2
public class JdbcOrganizationDao extends AbstractNamedObjectJdbcDao<Organization> implements OrganizationDao {

    private static final String Q_SELECT_ALL = "select \"id\", \"name\" FROM \"organization\"";
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\" FROM \"organization\" where \"name\" = ?";
    private static final String Q_SELECT_BY_ID = "select \"name\" FROM \"organization\" where \"id\" = ?";
    private static final String Q_INSERT = "INSERT INTO \"organization\" (\"id\", \"name\") VALUES (DEFAULT, ?)";

    public JdbcOrganizationDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull Organization create(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcOrganization(name, rs.getLong(1));
                } else {
                    throw new IllegalStateException("Can't receive generated id.");
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Organization> createAsync(@NotNull String name) {
        return supplyAsync(() -> create(name), fastDbTaskExecutor);
    }

    @Override
    public @Nullable Organization findByName(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_NAME)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcOrganization(
                        rs.getString(2),
                        rs.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return null;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Organization> findByNameAsync(@NotNull String name) {
        return supplyAsync(() -> findByName(name), fastDbTaskExecutor);
    }

    @Override
    public @Nullable Organization findById(long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_ID)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcOrganization(rs.getString(1), id);
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return null;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Organization> findByIdAsync(long id) {
        return supplyAsync(() -> findById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull Organization requireById(long id) {
        return notNull(findById(id));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Organization> requireByIdAsync(long id) {
        return supplyAsync(() -> requireById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull List<Organization> getAll() {

        var result = new ArrayList<Organization>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_ALL)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new JdbcOrganization(
                        rs.getString(2),
                        rs.getLong(1))
                    );
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<Organization>> getAllAsync() {
        return supplyAsync(this::getAll, slowDbTaskExecutor);
    }
}
