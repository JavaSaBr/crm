package com.ss.jcrm.dictionary.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultCountry;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import crm.jasync.function.JAsyncConverter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncCountryDao extends AbstractDictionaryDao<Country> implements CountryDao {

  private static final String FIELD_LIST = """
      "id", "name", "flag_code", "phone_code"
      """;

  private static final String Q_SELECT_BY_NAME = """
      select ${field-list} from "${schema}"."country" where "name" = ?
      """;

  private static final String Q_SELECT_BY_ID = """
      select ${field-list} from "${schema}"."country" where "id" = ?
      """;

  private static final String Q_SELECT_ALL = """
      select ${field-list} from "${schema}"."country"
      """;

  private static final String Q_INSERT = """
      insert into "${schema}"."country" ("name", "flag_code", "phone_code") values (?, ?, ?) returning "id"
      """;

  @NotNull String querySelectByName;
  @NotNull String querySelectById;
  @NotNull String querySelectAll;
  @NotNull String queryInsert;

  public JAsyncCountryDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool, @NotNull String schema) {
    super(connectionPool, schema, FIELD_LIST);
    this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
    this.querySelectAll = prepareQuery(Q_SELECT_ALL);
    this.queryInsert = prepareQuery(Q_INSERT);
    this.querySelectByName = prepareQuery(Q_SELECT_BY_NAME);
  }

  @Override
  protected @NotNull Class<Country> getEntityType() {
    return Country.class;
  }

  @Override
  public @NotNull Mono<Country> create(
      @NotNull String name,
      @NotNull String flagCode,
      @NotNull String phoneCode) {
    return insert(queryInsert,
        List.of(name, flagCode, phoneCode),
        id -> new DefaultCountry(name, flagCode, phoneCode, id));
  }

  @Override
  public @NotNull Flux<Country> findAll() {
    return selectAll(querySelectAll, converter());
  }

  @Override
  public @NotNull Mono<Country> findById(long id) {
    return select(querySelectById, List.of(id), converter());
  }

  @Override
  public @NotNull Mono<Country> findByName(@NotNull String name) {
    return select(querySelectByName, List.of(name), converter());
  }

  private @NotNull JAsyncConverter<JAsyncCountryDao, Country> converter() {
    return JAsyncCountryDao::toCountry;
  }

  private @NotNull DefaultCountry toCountry(@NotNull RowData data) {
    return new DefaultCountry(
        notNull(data.getString(1)),  // name
        notNull(data.getString(2)),  // flag code
        notNull(data.getString(3)),  // phone code
        notNull(data.getLong(0)));   // id
  }
}
