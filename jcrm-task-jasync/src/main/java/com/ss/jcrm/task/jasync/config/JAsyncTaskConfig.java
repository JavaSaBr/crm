package com.ss.jcrm.task.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import com.ss.jcrm.jasync.config.JAsyncConfig;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.task.api.dao.SimpleContactDao;
import com.ss.jcrm.task.jasync.dao.JAsyncSimpleContactDao;
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
public class JAsyncTaskConfig {

    @Autowired
    private Environment env;

    @Autowired
    private PoolConfiguration dbPoolConfiguration;

    @Autowired
    private EventLoopGroup dbEventLoopGroup;

    @Autowired
    private ExecutorService dbExecutor;

    @Bean
    @DependsOn("taskConnectionPool")
    @NotNull Flyway taskFlyway() {

        var flyway = Flyway.configure()
            .locations("classpath:com/ss/jcrm/task/db/migration")
            .baselineOnMigrate(true)
            .schemas(env.getRequiredProperty("jdbc.task.db.schema"))
            .dataSource(
                env.getRequiredProperty("jdbc.task.db.url"),
                env.getRequiredProperty("jdbc.task.db.username"),
                env.getRequiredProperty("jdbc.task.db.password")
            )
            .load();

        if (env.getProperty("db.upgrading.enabled", boolean.class, false)) {
            flyway.migrate();
        }

        return flyway;
    }

    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> taskConnectionPool() {

        var configuration = JAsyncUtils.buildConfiguration(
            env.getRequiredProperty("jdbc.task.db.username"),
            env.getRequiredProperty("jdbc.task.db.password"),
            env.getRequiredProperty("jdbc.task.db.host"),
            env.getRequiredProperty("jdbc.task.db.port", int.class),
            env.getRequiredProperty("jdbc.task.db.schema")
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
        @NotNull ConnectionPool<? extends ConcreteConnection> taskConnectionPool
    ) {
        return new JAsyncSimpleContactDao(taskConnectionPool, env.getRequiredProperty("jdbc.task.db.schema"));
    }
}

