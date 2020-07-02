package com.ss.jcrm.user.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.ifNull;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.jasync.dao.AbstractNamedObjectJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncLazyConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.impl.DefaultOrganization;
import com.ss.rlib.common.util.array.Array;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Log4j2
public class JAsyncOrganizationDao extends AbstractNamedObjectJAsyncDao<Organization> implements OrganizationDao {

    private static final String FIELD_LIST = "\"id\", \"name\", \"country_id\", \"version\", \"zip_code\"," +
        " \"address\", \"email\", \"phone_number\", \"city_id\", \"industries\"";

    private static final String Q_SELECT_ALL = "select " + FIELD_LIST + " from \"${schema}\".\"organization\"";
    private static final String Q_SELECT_BY_NAME = "select " + FIELD_LIST + " from \"${schema}\".\"organization\"" +
        " where \"name\" = ?";
    private static final String Q_SELECT_BY_ID = "select " + FIELD_LIST + " from \"${schema}\".\"organization\"" +
        " where \"id\" = ?";

    private static final String Q_INSERT = "insert into \"${schema}\".\"organization\" (\"name\", \"country_id\")" +
        " values (?, ?) RETURNING id";

    private static final String Q_EXIST_BY_NAME = "select \"id\" from \"${schema}\".\"organization\" where \"name\" = ?";

    private static final String Q_DELETE_BY_ID = "delete from \"${schema}\".\"organization\" where \"id\" = ?";

    private final String querySelectById;
    private final String querySelectByName;
    private final String querySelectAll;
    private final String queryInsert;
    private final String queryDeleteById;
    private final String queryExistByName;

    private final CityDao cityDao;
    private final IndustryDao industryDao;
    private final CountryDao countryDao;

    public JAsyncOrganizationDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull CityDao cityDao,
        @NotNull IndustryDao industryDao,
        @NotNull CountryDao countryDao
    ) {
        super(connectionPool);
        this.cityDao = cityDao;
        this.industryDao = industryDao;
        this.countryDao = countryDao;
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectByName = Q_SELECT_BY_NAME.replace("${schema}", schema);
        this.querySelectAll = Q_SELECT_ALL.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.queryDeleteById = Q_DELETE_BY_ID.replace("${schema}", schema);
        this.queryExistByName = Q_EXIST_BY_NAME.replace("${schema}", schema);
    }

    @Override
    protected @NotNull Class<Organization> getEntityType() {
        return Organization.class;
    }

    @Override
    public @NotNull Mono<@NotNull Organization> create(@NotNull String name, @NotNull Country country) {
        return insert(
            queryInsert,
            List.of(name, country.getId()),
            id -> new DefaultOrganization(id, 0, name, country)
        );
    }

    @Override
    public @NotNull Mono<@Nullable Organization> findByName(@NotNull String name) {
        return selectAsync(querySelectByName, name, convert());
    }

    @Override
    public @NotNull Mono<@Nullable Organization> findById(long id) {
        return selectAsync(querySelectById, id, convert());
    }

    @Override
    public @NotNull Mono<@NotNull Array<Organization>> findAll() {
        return selectAllAsync(querySelectAll, convert());
    }

    @Override
    public @NotNull Mono<Boolean> existByName(@NotNull String name) {
        return exist(queryExistByName, List.of(name));
    }

    @Override
    public @NotNull Mono<Boolean> delete(long id) {
        return delete(queryDeleteById, List.of(id));
    }

    private @NotNull JAsyncLazyConverter<@NotNull JAsyncOrganizationDao, @NotNull Organization> convert() {
        return JAsyncOrganizationDao::toOrganization;
    }

    private @NotNull Mono<@NotNull Organization> toOrganization(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var version = notNull(data.getInt(3));
        var countryId = ifNull(data.getLong(2), 0L);
        var cityId = ifNull(data.getLong(8), 0L);

        var name = data.getString(1);
        var zipCode = data.getString(4);
        var address = data.getString(5);
        var email = data.getString(6);
        var phoneNumber = data.getString(7);

        var asyncCountry = countryId > 0 ? countryDao.findById(countryId)
            .map(Optional::of) : Mono.just(Optional.<Country>empty());
        var asyncCity = cityId > 0 ? cityDao.findById(cityId)
            .map(Optional::of) : Mono.just(Optional.<City>empty());

        var asyncIndustries = JAsyncUtils.fromJsonIdsAsync(data.getString(9), industryDao, Dao::requireById);

        return Mono.zip(asyncCountry, asyncCity, asyncIndustries)
            .map(objects -> {

                var country = objects.getT1().orElse(null);
                var city = objects.getT2().orElse(null);
                var industries = objects.getT3();

                return new DefaultOrganization(
                    id,
                    version,
                    name,
                    zipCode,
                    address,
                    email,
                    phoneNumber,
                    city,
                    country,
                    industries
                );
            });
    }
}
