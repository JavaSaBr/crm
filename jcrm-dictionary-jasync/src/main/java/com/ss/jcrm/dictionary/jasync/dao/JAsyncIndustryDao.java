package com.ss.jcrm.dictionary.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.Industry;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultIndustry;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import jasync.function.JAsyncConverter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncIndustryDao extends AbstractDictionaryDao<Industry> implements IndustryDao {

  private static final String FIELD_LIST = """
      "id", "name"
      """;

  private static final String Q_SELECT_BY_NAME = """
      select ${field-list} from "${schema}"."industry" where "name" = ?
      """;

  private static final String Q_SELECT_BY_ID = """
      select ${field-list} from "${schema}"."industry" where "id" = ?
      """;

  private static final String Q_SELECT_ALL = """
      select ${field-list} from "${schema}"."industry"
      """;

  private static final String Q_INSERT = """
      insert into "${schema}"."industry" ("name") values (?) returning "id"
      """;

  @NotNull String querySelectByName;
  @NotNull String querySelectById;
  @NotNull String querySelectAll;
  @NotNull String queryInsert;

  public JAsyncIndustryDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool, @NotNull String schema) {
    super(connectionPool, schema, FIELD_LIST);
    this.querySelectByName = prepareQuery(Q_SELECT_BY_NAME);
    this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
    this.querySelectAll = prepareQuery(Q_SELECT_ALL);
    this.queryInsert = prepareQuery(Q_INSERT);
  }

  @Override
  protected @NotNull Class<Industry> getEntityType() {
    return Industry.class;
  }

  @Override
  public @NotNull Mono<@NotNull Industry> create(@NotNull String name) {
    return insert(queryInsert, List.of(name), id -> new DefaultIndustry(name, id));
  }

  @Override
  public @NotNull Flux<Industry> findAll() {
    return selectAll(querySelectAll, converter());
  }

  @Override
  public @NotNull Mono<Industry> findById(long id) {
    return select(querySelectById, id, converter());
  }

  @Override
  public @NotNull Mono<Industry> findByName(@NotNull String name) {
    return select(querySelectByName, name, converter());
  }

  private @NotNull JAsyncConverter<@NotNull JAsyncIndustryDao, @NotNull Industry> converter() {
    return JAsyncIndustryDao::toIndustry;
  }

  private @NotNull DefaultIndustry toIndustry(@NotNull RowData data) {
    return new DefaultIndustry(
        notNull(data.getString(1)), // name
        notNull(data.getLong(0)));  // id
  }
}
