package com.ss.jcrm.dictionary.jdbc;

import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.jdbc.dao.AbstractNamedObjectJdbcDao;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbsctractDictionaryDao<T> extends AbstractNamedObjectJdbcDao<T> implements DictionaryDao<T> {

    public AbsctractDictionaryDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<T>> findAllAsync() {
        return CompletableFuture.supplyAsync(this::findAll, slowDbTaskExecutor);
    }
}
