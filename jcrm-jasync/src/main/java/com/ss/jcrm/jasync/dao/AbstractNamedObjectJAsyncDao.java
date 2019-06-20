package com.ss.jcrm.jasync.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dao.NamedObjectDao;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbstractNamedObjectJAsyncDao<T extends NamedEntity> extends AbstractJAsyncDao<T> implements
    NamedObjectDao<T> {

    public AbstractNamedObjectJAsyncDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull CompletableFuture<T> findByNameAsync(@NotNull String name) {
        return supplyAsync(() -> findByName(name), fastDbTaskExecutor);
    }
}
