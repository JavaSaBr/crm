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
import lombok.extern.log4j.Log4j2;
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

@Log4j2
public class JdbcCityDao extends AbstractDictionaryDao<City> implements CityDao {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"country_id\" " +
        " FROM \"city\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"country_id\" " +
        " FROM \"city\" where \"id\" = ?";

    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"country_id\" " +
        " FROM \"city\"";

    private static final String Q_INSERT = "insert into \"city\" (\"name\", \"country_id\")" +
        " values (?, ?)";

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

        var result = new ArrayList<City>();
        var countries = countryDao.findAllAsMap();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_ALL)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {

                    var id = rs.getLong(1);
                    var name = rs.getString(2);
                    var countryId = rs.getLong(3);
                    var country = countries.get(countryId);

                    if (country == null) {
                        log.warn(
                            "Can't load a city \"{}\" because cannot find its country with the id {}.",
                            name,
                            countryId
                        );
                        continue;
                    }

                    result.add(new JdbcCity(name, country, id));
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return result;
    }

    @Override
    public @Nullable City findById(long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_ID)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return toCity(rs);
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    @Override
    public @Nullable City findByName(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_NAME)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return toCity(rs);
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    private @Nullable City toCity(@NotNull ResultSet rs) throws SQLException {

        var name = rs.getString(2);
        var countryId = rs.getLong(3);
        var country = countryDao.findById(countryId);

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
