package com.ss.jcrm.jdbc.dao;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

public abstract class AbstractJdbcDao {

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
}
