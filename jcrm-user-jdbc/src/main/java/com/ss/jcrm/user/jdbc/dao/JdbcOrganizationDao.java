package com.ss.jcrm.user.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.jdbc.dao.AbstractNamedObjectJdbcDao;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.jdbc.JdbcOrganization;
import com.ss.rlib.common.util.array.Array;
import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Log4j2
public class JdbcOrganizationDao extends AbstractNamedObjectJdbcDao<Organization> implements OrganizationDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"country_id\", \"version\"," +
        "\"zip_code\", \"address\", \"email\", \"phone_number\", \"city_id\", \"industries\"" +
        " from \"organization\"";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"country_id\", \"version\", " +
        "\"zip_code\", \"address\", \"email\", \"phone_number\", \"city_id\", \"industries\"" +
        " from \"organization\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"country_id\", \"version\"," +
        "\"zip_code\", \"address\", \"email\", \"phone_number\", \"city_id\", \"industries\"" +
        " from \"organization\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"organization\" (\"name\", \"country_id\") values (?, ?)";

    @Language("PostgreSQL")
    private static final String Q_EXIST_BY_NAME = "select \"id\" from \"organization\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_DELETE_BY_ID = "delete from \"organization\" where \"id\" = ?";

    private final CityDao cityDao;
    private final IndustryDao industryDao;
    private final CountryDao countryDao;

    public JdbcOrganizationDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor,
        @NotNull CityDao cityDao,
        @NotNull IndustryDao industryDao,
        @NotNull CountryDao countryDao
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
        this.cityDao = cityDao;
        this.industryDao = industryDao;
        this.countryDao = countryDao;
    }

    @Override
    public @NotNull Organization create(@NotNull String name, @NotNull Country country) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.setLong(2, country.getId());
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcOrganization(rs.getLong(1), 0, name, country);
                } else {
                    throw new IllegalStateException("Can't receive generated id.");
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Organization> createAsync(
        @NotNull String name,
        @NotNull Country country
    ) {
        return supplyAsync(() -> create(name, country), fastDbTaskExecutor);
    }

    @Override
    public @Nullable Organization findByName(@NotNull String name) {
        return findByString(Q_SELECT_BY_NAME, name, JdbcOrganizationDao::toOrganization);
    }

    @Override
    public @Nullable Organization findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcOrganizationDao::toOrganization);
    }

    @Override
    public @NotNull Array<Organization> findAll() {
        return findAll(Organization.class, Q_SELECT_ALL, JdbcOrganizationDao::toOrganization);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Array<Organization>> getAllAsync() {
        return supplyAsync(this::findAll, slowDbTaskExecutor);
    }

    @Override
    public boolean existByName(@NotNull String name) {
        return existByString(Q_EXIST_BY_NAME, name);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> existByNameAsync(@NotNull String name) {
        return supplyAsync(() -> existByName(name), fastDbTaskExecutor);
    }

    @Override
    public boolean delete(long id) {
        return deleteByLong(Q_DELETE_BY_ID, id);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAsync(long id) {
        return supplyAsync(() -> delete(id), fastDbTaskExecutor);
    }

    private @NotNull Organization toOrganization(@NotNull ResultSet rs) throws SQLException {

        var countryId = rs.getLong(3);
        var cityId = rs.getLong(9);

        var country = countryId > 0 ? countryDao.findById(countryId) : null;
        var city = cityId > 0 ? cityDao.findById(cityId) : null;

        var industries = JdbcUtils.fromJsonArray(rs.getString(10), industryDao, Dao::requireById);

        return new JdbcOrganization(
            rs.getLong(1),   // id
            rs.getInt(4),    // version
            rs.getString(2), // name
            rs.getString(5), // zip code
            rs.getString(6), // address
            rs.getString(7), // email
            rs.getString(8), // phone number
            city,
            country,
            industries
        );
    }
}
