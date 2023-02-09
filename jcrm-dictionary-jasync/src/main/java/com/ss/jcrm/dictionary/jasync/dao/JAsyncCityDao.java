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
import jasync.function.JAsyncLazyConverter;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncCityDao extends AbstractDictionaryDao<City> implements CityDao {

  private static final String FIELD_LIST = """
      "id", "name", "country_id"
      """;

  private static final String Q_SELECT_BY_NAME = """
      select ${field-list} from "${schema}"."city" where "name" = ?
      """;

  private static final String Q_SELECT_BY_ID = """
      select ${field-list} from "${schema}"."city" where "id" = ?
      """;

  private static final String Q_SELECT_ALL = """
      select ${field-list} from "${schema}"."city"
      """;

  private static final String Q_INSERT = """
      insert into "${schema}"."city" ("name", "country_id") values (?, ?) returning "id"
      """;

  @NotNull String querySelectByName;
  @NotNull String querySelectById;
  @NotNull String querySelectAll;
  @NotNull String queryInsert;

  @NotNull CountryDao countryDao;

  public JAsyncCityDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
      @NotNull String schema,
      @NotNull CountryDao countryDao) {
    super(connectionPool, schema, FIELD_LIST);
    this.querySelectByName = prepareQuery(Q_SELECT_BY_NAME);
    this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
    this.querySelectAll = prepareQuery(Q_SELECT_ALL);
    this.queryInsert = prepareQuery(Q_INSERT);
    this.countryDao = countryDao;
  }

  @Override
  protected @NotNull Class<City> getEntityType() {
    return City.class;
  }

  @Override
  public @NotNull Mono<@NotNull City> create(@NotNull String name, @NotNull Country country) {
    return insert(queryInsert, List.of(name, country.id()), id -> new DefaultCity(name, country, id));
  }

  @Override
  public @NotNull Flux<City> findAll() {
    return countryDao
        .findAllAsMap()
        .flatMapMany(countries -> selectAll(querySelectAll, countries, JAsyncCityDao::toCity));
  }

  @Override
  public @NotNull Mono<City> findById(long id) {
    return selectAsync(querySelectById, List.of(id), converter());
  }

  @Override
  public @NotNull Mono<City> findByName(@NotNull String name) {
    return selectAsync(querySelectByName, List.of(name), converter());
  }

  private @NotNull JAsyncLazyConverter<@NotNull JAsyncCityDao, @NotNull City> converter() {
    return JAsyncCityDao::toAsyncCity;
  }

  private @Nullable City toCity(@NotNull LongDictionary<Country> countries, @NotNull RowData data) {

    var name = notNull(data.getString(1));
    var countryId = notNull(data.getLong(2));
    var country = countries.get(countryId);

    if (country == null) {
      log.warn("Can't load a city \"{}\" because cannot find its country with the id {}.", name, countryId);
      return null;
    }

    return new DefaultCity(name, country, notNull(data.getLong(0)));
  }

  private @NotNull Mono<City> toAsyncCity(@NotNull RowData data) {

    var name = notNull(data.getString(1));
    var countryId = notNull(data.getLong(2));

    return countryDao
        .findById(countryId)
        .map(country -> new DefaultCity(name, country, notNull(data.getLong(0))));
  }
}
