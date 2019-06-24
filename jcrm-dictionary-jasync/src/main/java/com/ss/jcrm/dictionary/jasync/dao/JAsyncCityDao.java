package com.ss.jcrm.dictionary.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultCity;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class JAsyncCityDao extends AbstractDictionaryDao<City> implements CityDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"country_id\" " +
        " from \"${schema}\".\"city\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"country_id\" " +
        " from \"${schema}\".\"city\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"country_id\" from \"${schema}\".\"city\"";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"${schema}\".\"city\" (\"name\", \"country_id\") " +
        "values (?, ?)  RETURNING id";

    private final String querySelectByName;
    private final String querySelectById;
    private final String querySelectAll;
    private final String queryInsert;

    private final CountryDao countryDao;

    public JAsyncCityDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull CountryDao countryDao
    ) {
        super(connectionPool);
        this.querySelectByName = Q_SELECT_BY_NAME.replace("${schema}", schema);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectAll = Q_SELECT_ALL.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.countryDao = countryDao;
    }

    @Override
    public @NotNull City create(@NotNull String name, @NotNull Country country) {
        return JAsyncUtils.unwrapJoin(createAsync(name, country));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull City> createAsync(@NotNull String name, @NotNull Country country) {
        return connectionPool.sendPreparedStatement(queryInsert, List.of(name, country.getId()))
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();
                var id = notNull(rset.get(0).getLong(0));

                return new DefaultCity(name, country, id);
            });
    }

    @Override
    public @NotNull Array<City> findAll() {
        return JAsyncUtils.unwrapJoin(findAllAsync());
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Array<City>> findAllAsync() {
        return findAll(City.class, querySelectAll, countryDao.findAllAsMap(), JAsyncCityDao::toCities);
    }

    @Override
    public @NotNull CompletableFuture<City> findByIdAsync(long id) {
        return findByAndCompose(querySelectById, id, JAsyncCityDao::toCity);
    }

    @Override
    public @NotNull CompletableFuture<City> findByNameAsync(@NotNull String name) {
        return findByAndCompose(querySelectByName, name, JAsyncCityDao::toCity);
    }

    private @Nullable City toCities(@NotNull LongDictionary<Country> countries, @NotNull RowData data) {

        var name = notNull(data.getString(1));
        var countryId = notNull(data.getLong(2));
        var country = countries.get(countryId);

        if (country == null) {
            log.warn(
                "Can't load a city \"{}\" because cannot find its country with the id {}.",
                name,
                countryId
            );
            return null;
        }

        return new DefaultCity(name, country, notNull(data.getLong(0)));
    }

    private @NotNull CompletableFuture<@Nullable City> toCity(@NotNull RowData data) {

        var name = notNull(data.getString(1));
        var countryId = notNull(data.getLong(2));

        return countryDao.findByIdAsync(countryId)
            .thenApply(country -> {

                if (country == null) {
                    log.warn(
                        "Can't load a city \"{}\" because cannot find its country with the id {}.",
                        name,
                        countryId
                    );
                    return null;
                }

                return new DefaultCity(name, country, notNull(data.getLong(0)));
            });
    }
}
