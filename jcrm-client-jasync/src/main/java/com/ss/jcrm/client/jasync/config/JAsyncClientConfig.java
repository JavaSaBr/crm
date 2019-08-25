package com.ss.jcrm.client.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import com.ss.jcrm.jasync.config.JAsyncConfig;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.client.jasync.dao.JAsyncSimpleContactDao;
import io.netty.channel.EventLoopGroup;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;

@Configuration
@Import(JAsyncConfig.class)
public class JAsyncClientConfig {

    @Autowired
    private Environment env;

    @Autowired
    private PoolConfiguration dbPoolConfiguration;

    @Autowired
    private EventLoopGroup dbEventLoopGroup;

    @Autowired
    private ExecutorService dbExecutor;

    @Bean
    @DependsOn("clientConnectionPool")
    @NotNull Flyway clientFlyway() {

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
    @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool() {

        var configuration = JAsyncUtils.buildConfiguration(
            env.getRequiredProperty("jdbc.client.db.username"),
            env.getRequiredProperty("jdbc.client.db.password"),
            env.getRequiredProperty("jdbc.client.db.host"),
            env.getRequiredProperty("jdbc.client.db.port", int.class),
            env.getRequiredProperty("jdbc.client.db.schema")
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
    @NotNull SimpleContactDao simpleContactDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool
    ) {
        return new JAsyncSimpleContactDao(clientConnectionPool, env.getRequiredProperty("jdbc.client.db.schema"));
    }
}

