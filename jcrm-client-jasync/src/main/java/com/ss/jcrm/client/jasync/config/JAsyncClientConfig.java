package com.ss.jcrm.client.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import crm.client.api.dao.SimpleClientDao;
import com.ss.jcrm.client.jasync.dao.JAsyncSimpleClientDao;
import jasync.config.CommonJAsyncConfig;
import jasync.util.JAsyncUtils;
import io.netty.channel.EventLoopGroup;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;

@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/client/jasync/client-jasync.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/client/jasync/client-jasync-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@Import(CommonJAsyncConfig.class)
@Configuration(proxyBeanMethods = false)
public class JAsyncClientConfig {

    @Bean
    @NotNull Flyway clientFlyway(
        @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool,
        @NotNull Environment env
    ) {

        var flyway = Flyway.configure()
            .locations("classpath:com/ss/jcrm/client/db/migration")
            .baselineOnMigrate(true)
            .schemas(env.getRequiredProperty("jdbc.client.db.schema"))
            .dataSource(
                env.getRequiredProperty("jdbc.client.db.url"),
                env.getRequiredProperty("jdbc.client.db.username"),
                env.getRequiredProperty("jdbc.client.db.password")
            )
            .load();

        if (env.getProperty("db.upgrading.enabled", boolean.class, false)) {
            flyway.migrate();
        }

        return flyway;
    }

    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool(
        @NotNull PoolConfiguration dbPoolConfiguration,
        @NotNull EventLoopGroup dbEventLoopGroup,
        @NotNull ExecutorService dbExecutor,
        @NotNull Environment env
    ) {

        var configuration = JAsyncUtils.buildConfiguration(
            env.getRequiredProperty("jdbc.client.db.username"),
            env.getRequiredProperty("jdbc.client.db.password"),
            env.getRequiredProperty("jdbc.client.db.host"),
            env.getRequiredProperty("jdbc.client.db.port", int.class),
            env.getRequiredProperty("jdbc.client.db.database")
        );

        var connectionPoolConfiguration = JAsyncUtils.buildPoolConfig(
            configuration,
            dbPoolConfiguration,
            dbEventLoopGroup,
            dbExecutor
        );

        return new ConnectionPool<>(
            new PostgreSQLConnectionFactory(configuration),
            connectionPoolConfiguration
        );
    }

    @Bean
    @NotNull String clientDbSchema(@NotNull Environment env) {
        return env.getRequiredProperty("jdbc.client.db.schema");
    }

    @Bean
    @NotNull SimpleClientDao simpleContactDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool,
        @NotNull String clientDbSchema
    ) {
        return new JAsyncSimpleClientDao(clientConnectionPool, clientDbSchema);
    }
}

