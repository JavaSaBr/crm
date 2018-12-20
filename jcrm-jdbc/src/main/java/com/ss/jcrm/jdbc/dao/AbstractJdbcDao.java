package com.ss.jcrm.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.rlib.common.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbstractJdbcDao<T extends Entity> implements Dao<T> {

    protected final DataSource dataSource;
    protected final Executor fastDbTaskExecutor;
    protected final Executor slowDbTaskExecutor;

    public AbstractJdbcDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        this.dataSource = dataSource;
        this.fastDbTaskExecutor = fastDbTaskExecutor;
        this.slowDbTaskExecutor = slowDbTaskExecutor;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable T> findByIdAsync(long id) {
        return supplyAsync(() -> findById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull T requireById(long id) {
        return ObjectUtils.notNull(findById(id), id,
            value -> new ObjectNotFoundDaoException("Can't find an entity with the id " + value));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull T> requireByIdAsync(long id) {
        return supplyAsync(() -> requireById(id), fastDbTaskExecutor);
    }
}
