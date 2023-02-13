package crm.user.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.ifNull;
import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.dao.Dao;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import jasync.dao.AbstractNamedObjectJAsyncDao;
import jasync.function.JAsyncLazyConverter;
import jasync.util.JAsyncUtils;
import crm.user.api.Organization;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.impl.DefaultOrganization;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncOrganizationDao extends AbstractNamedObjectJAsyncDao<Organization> implements OrganizationDao {

  private static final String FIELD_LIST = """
      "id", "name", "country_id", "version", "zip_code", "address", "email", "phone_number", "city_id", "industries"
      """;

  private static final String Q_SELECT_ALL = """
      select ${field-list} from "${schema}"."organization"
      """;

  private static final String Q_SELECT_BY_NAME = """
      select ${field-list} from "${schema}"."organization" where "name" = ?
      """;

  private static final String Q_SELECT_BY_ID = """
      select ${field-list} from "${schema}"."organization" where "id" = ?
      """;

  private static final String Q_INSERT = """
      insert into "${schema}"."organization" ("name", "country_id") values (?, ?) returning "id"
      """;

  private static final String Q_EXIST_BY_NAME = """
      select "id" from "${schema}"."organization" where "name" = ?
      """;

  private static final String Q_DELETE_BY_ID = """
      delete from "${schema}"."organization" where "id" = ?
      """;

  @NotNull String querySelectById;
  @NotNull String querySelectByName;
  @NotNull String querySelectAll;
  @NotNull String queryInsert;
  @NotNull String queryDeleteById;
  @NotNull String queryExistByName;

  @NotNull CityDao cityDao;
  @NotNull IndustryDao industryDao;
  @NotNull CountryDao countryDao;

  public JAsyncOrganizationDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
      @NotNull String schema,
      @NotNull CityDao cityDao,
      @NotNull IndustryDao industryDao,
      @NotNull CountryDao countryDao) {
    super(connectionPool, schema, FIELD_LIST);
    this.cityDao = cityDao;
    this.industryDao = industryDao;
    this.countryDao = countryDao;
    this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
    this.querySelectByName = prepareQuery(Q_SELECT_BY_NAME);
    this.querySelectAll = prepareQuery(Q_SELECT_ALL);
    this.queryInsert = prepareQuery(Q_INSERT);
    this.queryDeleteById = prepareQuery(Q_DELETE_BY_ID);
    this.queryExistByName = prepareQuery(Q_EXIST_BY_NAME);
  }

  @Override
  public @NotNull Mono<Organization> create(@NotNull String name, @NotNull Country country) {
    return insert(queryInsert, List.of(name, country.id()))
        .map(id -> new DefaultOrganization(id, 0, name, country));
  }

  @Override
  public @NotNull Mono<Organization> findByName(@NotNull String name) {
    return selectAsync(querySelectByName, name, convert());
  }

  @Override
  public @NotNull Mono<Organization> findById(long id) {
    return selectAsync(querySelectById, id, convert());
  }

  @Override
  public @NotNull Flux<Organization> findAll() {
    return selectAllAsync(querySelectAll, convert());
  }

  @Override
  public @NotNull Mono<Boolean> existByName(@NotNull String name) {
    return exist(queryExistByName, List.of(name));
  }

  @Override
  public @NotNull Mono<Boolean> deleteById(long id) {
    return delete(queryDeleteById, List.of(id));
  }

  private @NotNull JAsyncLazyConverter<@NotNull JAsyncOrganizationDao, @NotNull Organization> convert() {
    return JAsyncOrganizationDao::toOrganization;
  }

  private @NotNull Mono<Organization> toOrganization(@NotNull RowData data) {

    long id = notNull(data.getLong(0));
    long countryId = ifNull(data.getLong(2), 0L);
    long cityId = ifNull(data.getLong(8), 0L);
    int version = notNull(data.getInt(3));

    var name = notNull(data.getString(1));
    var zipCode = data.getString(4);
    var address = data.getString(5);
    var email = data.getString(6);
    var phoneNumber = data.getString(7);

    var asyncCountry = countryId > 0 ? countryDao
        .findById(countryId)
        .map(Optional::of) : Mono.just(Optional.<Country>empty());
    var asyncCity = cityId > 0 ? cityDao
        .findById(cityId)
        .map(Optional::of) : Mono.just(Optional.<City>empty());
    var asyncIndustries = JAsyncUtils.fromJsonIdsAsync(
        data.getString(9),
        industryDao,
        Dao::requireById);

    return Mono
        .zip(asyncCountry, asyncCity, asyncIndustries)
        .map(dependencies -> {

          var country = dependencies
              .getT1()
              .orElse(null);
          var city = dependencies
              .getT2()
              .orElse(null);
          var industries = dependencies.getT3();

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
              industries);
        });
  }
}
