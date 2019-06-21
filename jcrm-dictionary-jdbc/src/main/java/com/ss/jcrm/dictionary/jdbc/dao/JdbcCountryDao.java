package com.ss.jcrm.dictionary.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.GenerateIdDaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultCountry;
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

public class JdbcCountryDao extends AbstractDictionaryDao<Country> implements CountryDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"country\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"country\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"country\"";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"country\" (\"name\", \"flag_code\", \"phone_code\")" +
        " values (?, ?, ?)";

    public JdbcCountryDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull Country create(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.setString(2, flagCode);
            statement.setString(3, phoneCode);
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new DefaultCountry(
                        name,
                        flagCode,
                        phoneCode,
                        rs.getLong(1)
                    );
                } else {
                    throw new GenerateIdDaoException("Can't receive generated id for the new country entity.");
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Country> createAsync(
        @NotNull String name,
        @NotNull String flagCode,
        @NotNull String phoneCode
    ) {
        return supplyAsync(() -> create(name, flagCode, phoneCode), fastDbTaskExecutor);
    }

    @Override
    public @NotNull Array<Country> findAll() {
        return findAll(Country.class, Q_SELECT_ALL, JdbcCountryDao::toCountry);
    }

    @Override
    public @Nullable Country findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcCountryDao::toCountry);
    }

    @Override
    public @Nullable Country findByName(@NotNull String name) {
        return findByString(Q_SELECT_BY_NAME, name, JdbcCountryDao::toCountry);
    }

    private @NotNull DefaultCountry toCountry(@NotNull ResultSet rs) throws SQLException {
        return new DefaultCountry(
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getLong(1)
        );
    }
}
