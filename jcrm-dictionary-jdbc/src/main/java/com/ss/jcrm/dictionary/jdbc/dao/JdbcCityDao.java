package com.ss.jcrm.dictionary.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.GenerateIdDaoException;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jdbc.AbstractDictionaryDao;
import com.ss.jcrm.dictionary.jdbc.JdbcCity;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Log4j2
public class JdbcCityDao extends AbstractDictionaryDao<City> implements CityDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"country_id\" " +
        " from \"city\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"country_id\" " +
        " from \"city\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"country_id\" from \"city\"";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"city\" (\"name\", \"country_id\") values (?, ?)";

    private final CountryDao countryDao;

    public JdbcCityDao(
        @NotNull DataSource dataSource,
        @NotNull CountryDao countryDao,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
        this.countryDao = countryDao;
    }

    @Override
    public @NotNull City create(@NotNull String name, @NotNull Country country) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.setLong(2, country.getId());
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcCity(
                        name,
                        country,
                        rs.getLong(1)
                    );
                } else {
                    throw new GenerateIdDaoException("Can't receive generated id for the new city entity.");
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull City> createAsync(@NotNull String name, @NotNull Country country) {
        return supplyAsync(() -> create(name, country), fastDbTaskExecutor);
    }

    @Override
    public @NotNull List<City> findAll() {
        return findAll(Q_SELECT_ALL, countryDao.findAllAsMap(), JdbcCityDao::toCities);
    }

    @Override
    public @Nullable City findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcCityDao::toCity);
    }

    @Override
    public @Nullable City findByName(@NotNull String name) {
        return findByString(Q_SELECT_BY_NAME, name, JdbcCityDao::toCity);
    }

    private @Nullable City toCity(@NotNull ResultSet rs) throws SQLException {
        return toCities(null, rs);
    }

    private @Nullable City toCities(@Nullable LongDictionary<Country> countries, @NotNull ResultSet rs)
        throws SQLException {

        var name = rs.getString(2);
        var countryId = rs.getLong(3);
        var country = countries == null? countryDao.findById(countryId) : countries.get(countryId);

        if (country == null) {
            log.warn(
                "Can't load a city \"{}\" because cannot find its country with the id {}.",
                name,
                countryId
            );
            return null;
        }

        return new JdbcCity(name, country, rs.getLong(1));
    }
}
