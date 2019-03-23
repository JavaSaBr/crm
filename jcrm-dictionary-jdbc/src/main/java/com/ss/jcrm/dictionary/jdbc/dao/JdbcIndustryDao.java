package com.ss.jcrm.dictionary.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.GenerateIdDaoException;
import com.ss.jcrm.dictionary.api.Industry;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.jdbc.AbstractDictionaryDao;
import com.ss.jcrm.dictionary.jdbc.JdbcIndustry;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JdbcIndustryDao extends AbstractDictionaryDao<Industry> implements IndustryDao {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\" FROM \"industry\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\" FROM \"industry\" where \"id\" = ?";

    private static final String Q_SELECT_ALL = "select \"id\", \"name\" FROM \"industry\"";

    private static final String Q_INSERT = "insert into \"industry\" (\"name\") values (?)";

    public JdbcIndustryDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull Industry create(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcIndustry(name, rs.getLong(1));
                } else {
                    throw new GenerateIdDaoException("Can't receive generated id for the new industry entity.");
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Industry> createAsync(@NotNull String name) {
        return supplyAsync(() -> create(name), fastDbTaskExecutor);
    }

    @Override
    public @NotNull List<Industry> findAll() {

        var result = new ArrayList<Industry>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_ALL)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(toIndustry(rs));
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return result;
    }

    @Override
    public @Nullable Industry findById(long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_ID)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return toIndustry(rs);
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    @Override
    public @Nullable Industry findByName(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_NAME)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return toIndustry(rs);
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    private @NotNull JdbcIndustry toIndustry(@NotNull ResultSet rs) throws SQLException {
        return new JdbcIndustry(rs.getString(2), rs.getLong(1));
    }
}
