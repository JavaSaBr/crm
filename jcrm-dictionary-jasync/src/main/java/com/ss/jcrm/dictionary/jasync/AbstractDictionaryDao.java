package com.ss.jcrm.dictionary.jasync;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.jasync.dao.AbstractNamedObjectJAsyncDao;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractDictionaryDao<T extends NamedEntity> extends AbstractNamedObjectJAsyncDao<T> implements
    DictionaryDao<T> {

    public AbstractDictionaryDao(@NotNull ConnectionPool<? extends ConcreteConnection> connectionPool) {
        super(connectionPool);
    }

    @Override
    public @NotNull Array<T> findAll() {
        return findAllAsync().join();
    }

    @Override
    public @NotNull LongDictionary<T> findAllAsMap() {
        return findAllAsMapAsync().join();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull LongDictionary<T>> findAllAsMapAsync() {
        return findAllAsync()
            .thenApply(array -> {
                var result = DictionaryFactory.<T>newLongDictionary(array.size());
                array.forEach(result, (element, dictionary) -> dictionary.put(element.getId(), element));
                return result;
            });
    }
}
