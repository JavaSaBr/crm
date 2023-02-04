package com.ss.jcrm.dictionary.jasync;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.jasync.dao.AbstractNamedObjectJAsyncDao;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public abstract class AbstractDictionaryDao<T extends NamedUniqEntity> extends
    AbstractNamedObjectJAsyncDao<T> implements DictionaryDao<T> {

  protected AbstractDictionaryDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
      @NotNull String schema,
      @NotNull String fieldList) {
    super(connectionPool, schema, fieldList);
  }

  @Override
  public @NotNull Mono<@NotNull LongDictionary<T>> findAllAsMap() {
    return findAll()
        .collectList()
        .map(list -> {
          var result = DictionaryFactory.<T>newLongDictionary(list.size());
          list.forEach(element -> result.put(element.id(), element));
          return result;
        });
  }
}
