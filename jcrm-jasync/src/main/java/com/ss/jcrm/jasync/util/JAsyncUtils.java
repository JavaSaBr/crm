package com.ss.jcrm.jasync.util;

import static java.util.Collections.emptyList;
import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.exceptions.GenericDatabaseException;
import com.github.jasync.sql.db.util.ExecutorServiceUtils;
import com.github.jasync.sql.db.util.NettyUtils;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.rlib.common.util.ObjectUtils;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class JAsyncUtils {

    public static <T> T unwrapJoin(@NotNull CompletableFuture<T> future) {
        try {
            return future.join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }

    public static <T> @NotNull BiFunction<T, Throwable, T> handleException() {

        return (result, throwable) -> {

            var exc = throwable;

            if (exc instanceof CompletionException) {
                exc = exc.getCause();
            }

            if (exc instanceof GenericDatabaseException) {
                throw convert((GenericDatabaseException) exc);
            } else if (exc instanceof RuntimeException) {
                throw (RuntimeException) exc;
            } else if (exc != null) {
                throw new RuntimeException(exc);
            }

            return result;
        };
    }

    public static  @NotNull DaoException convert(@NotNull GenericDatabaseException exception) {

        var errorMessage = exception.getErrorMessage();
        var fields = errorMessage.getFields();


        switch (fields.get('C')) {
            case "23505": {
                return new DuplicateObjectDaoException(String.valueOf(errorMessage.getMessage()));
            }
        }

        return new DaoException(exception);
    }

    public static @NotNull ConnectionPoolConfiguration buildPoolConfig(
        @NotNull Configuration configuration,
        @NotNull PoolConfiguration dbPoolConfiguration,
        @Nullable EventLoopGroup eventLoopGroup,
        @Nullable Executor dbExecutor
    ) {

        return new ConnectionPoolConfiguration(
            configuration.getHost(),
            configuration.getPort(),
            configuration.getDatabase(),
            configuration.getUsername(),
            configuration.getPassword(),
            dbPoolConfiguration.getMaxObjects(),
            dbPoolConfiguration.getMaxIdle(),
            dbPoolConfiguration.getMaxQueueSize(),
            dbPoolConfiguration.getValidationInterval(),
            dbPoolConfiguration.getCreateTimeout(),
            dbPoolConfiguration.getTestTimeout(),
            dbPoolConfiguration.getQueryTimeout(),
            ObjectUtils.ifNull(eventLoopGroup, NettyUtils.INSTANCE.getDefaultEventLoopGroup()),
            ObjectUtils.ifNull(dbExecutor, ExecutorServiceUtils.INSTANCE.getCommonPool()),
            dbPoolConfiguration.getCoroutineDispatcher(),
            configuration.getSsl(),
            configuration.getCharset(),
            configuration.getMaximumMessageSize(),
            PooledByteBufAllocator.DEFAULT,
            "JCRM",
            emptyList(),
            dbPoolConfiguration.getMaxObjectTtl()
        );
    }

    public static @NotNull ConnectionPoolConfiguration buildPoolConfig(
        @NotNull Configuration configuration,
        @NotNull PoolConfiguration dbPoolConfiguration
    ) {
        return buildPoolConfig(configuration, dbPoolConfiguration, null, null);
    }

    public static @NotNull Configuration buildConfiguration(
        @NotNull String username,
        @NotNull String password,
        @NotNull String host,
        int port,
        @NotNull String schema
    ) {
        return new Configuration(
            username,
            host,
            port,
            password,
            schema
        );
    }
}
