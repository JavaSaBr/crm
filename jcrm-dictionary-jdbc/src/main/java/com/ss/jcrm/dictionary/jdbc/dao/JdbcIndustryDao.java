package com.ss.jcrm.dictionary.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.GenerateIdDaoException;
import com.ss.jcrm.dictionary.api.Industry;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultIndustry;
import com.ss.jcrm.dictionary.jdbc.AbstractDictionaryDao;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import com.ss.rlib.common.util.array.Array;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JdbcIndustryDao extends AbstractDictionaryDao<Industry> implements IndustryDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\" from \"industry\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\" from \"industry\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\" from \"industry\"";

    @Language("PostgreSQL")
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
                    return new DefaultIndustry(name, rs.getLong(1));
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
    public @NotNull Array<Industry> findAll() {
        return findAll(Industry.class, Q_SELECT_ALL, JdbcIndustryDao::toIndustry);
    }

    @Override
    public @Nullable Industry findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcIndustryDao::toIndustry);
    }

    @Override
    public @Nullable Industry findByName(@NotNull String name) {
        return findByString(Q_SELECT_BY_NAME, name, JdbcIndustryDao::toIndustry);
    }

    private @NotNull DefaultIndustry toIndustry(@NotNull ResultSet rs) throws SQLException {
        return new DefaultIndustry(rs.getString(2), rs.getLong(1));
    }
}
