package com.ss.jcrm.dictionary.jasync;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.jdbc.dao.AbstractNamedObjectJdbcDao;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbstractDictionaryDao<T extends NamedEntity> extends AbstractNamedObjectJdbcDao<T> implements
    DictionaryDao<T> {

    public AbstractDictionaryDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<T>> findAllAsync() {
        return supplyAsync(this::findAll, slowDbTaskExecutor);
    }

    @Override
    public @NotNull LongDictionary<T> findAllAsMap() {

        var result = DictionaryFactory.<T>newLongDictionary();

        for (var entity : findAll()) {
            result.put(entity.getId(), entity);
        }

        return result;
    }

    @Override
    public @NotNull CompletableFuture<@NotNull LongDictionary<T>> findAllAsMapAsync() {
        return supplyAsync(this::findAllAsMap, slowDbTaskExecutor);
    }
}
