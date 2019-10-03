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
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
public class JAsyncCityDao extends AbstractDictionaryDao<City> implements CityDao {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"country_id\" " +
        " from \"${schema}\".\"city\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"country_id\" " +
        " from \"${schema}\".\"city\" where \"id\" = ?";

    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"country_id\" from \"${schema}\".\"city\"";

    private static final String Q_INSERT = "insert into \"${schema}\".\"city\" (\"name\", \"country_id\") " +
        "values (?, ?) returning id";

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
    protected @NotNull Class<City> getEntityType() {
        return City.class;
    }

    @Override
    public @NotNull Mono<@NotNull City> create(@NotNull String name, @NotNull Country country) {
        return insert(
            queryInsert,
            List.of(name, country.getId()),
            id -> new DefaultCity(name, country, id)
        );
    }

    @Override
    public @NotNull Mono<@NotNull Array<City>> findAll() {
        return countryDao.findAllAsMap()
            .flatMap(countries -> selectAll(querySelectAll, countries, JAsyncCityDao::toCities));
    }

    @Override
    public @NotNull Mono<City> findById(long id) {
        return selectAsync(querySelectById, List.of(id), JAsyncCityDao::toCity);
    }

    @Override
    public @NotNull Mono<City> findByName(@NotNull String name) {
        return selectAsync(querySelectByName, List.of(name), JAsyncCityDao::toCity);
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

    private @NotNull Mono<@Nullable City> toCity(@NotNull RowData data) {

        var name = notNull(data.getString(1));
        var countryId = notNull(data.getLong(2));

        return countryDao.findById(countryId)
            .map(country -> new DefaultCity(name, country, notNull(data.getLong(0))));
    }
}
