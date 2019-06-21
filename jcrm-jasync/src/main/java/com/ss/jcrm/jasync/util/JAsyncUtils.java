package com.ss.jcrm.jasync.util;

import static java.util.Collections.emptyList;
import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.util.ExecutorServiceUtils;
import com.github.jasync.sql.db.util.NettyUtils;
import com.ss.rlib.common.util.ObjectUtils;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

public class JAsyncUtils {

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
