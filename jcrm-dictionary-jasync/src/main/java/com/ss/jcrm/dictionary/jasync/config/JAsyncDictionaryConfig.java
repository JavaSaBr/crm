package com.ss.jcrm.dictionary.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.jasync.dao.JAsyncCityDao;
import com.ss.jcrm.dictionary.jasync.dao.JAsyncCountryDao;
import com.ss.jcrm.dictionary.jasync.dao.JAsyncIndustryDao;
import com.ss.jcrm.jasync.config.JAsyncConfig;
import com.ss.jcrm.jasync.util.JAsyncUtils;
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
public class JAsyncDictionaryConfig {

    @Autowired
    private Environment env;

    @Autowired
    private PoolConfiguration dbPoolConfiguration;

    @Autowired
    private EventLoopGroup dbEventLoopGroup;

    @Autowired
    private ExecutorService dbExecutor;

    @Bean
    @DependsOn("dictionaryConnectionPool")
    @NotNull Flyway dictionaryFlyway() {

        var flyway = Flyway.configure()
            .locations("classpath:com/ss/jcrm/dictionary/db/migration")
            .baselineOnMigrate(true)
            .schemas(env.getRequiredProperty("jdbc.dictionary.db.schema"))
            .dataSource(
                env.getRequiredProperty("jdbc.dictionary.db.url"),
                env.getRequiredProperty("jdbc.dictionary.db.username"),
                env.getRequiredProperty("jdbc.dictionary.db.password")
            )
            .load();

        if (env.getProperty("db.upgrading.enabled", boolean.class, false)) {
            flyway.migrate();
        }

        return flyway;
    }

    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool() {

        var configuration = JAsyncUtils.buildConfiguration(
            env.getRequiredProperty("jdbc.dictionary.db.username"),
            env.getRequiredProperty("jdbc.dictionary.db.password"),
            env.getRequiredProperty("jdbc.dictionary.db.host"),
            env.getRequiredProperty("jdbc.dictionary.db.port", int.class),
            env.getRequiredProperty("jdbc.dictionary.db.database")
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
    @NotNull CountryDao countryDao() {
        return new JAsyncCountryDao(
            dictionaryConnectionPool(),
            env.getRequiredProperty("jdbc.dictionary.db.schema")
        );
    }

    @Bean
    @NotNull CityDao cityDao() {
        return new JAsyncCityDao(
            dictionaryConnectionPool(),
            env.getRequiredProperty("jdbc.dictionary.db.schema"),
            countryDao()
        );
    }

    @Bean
    @NotNull IndustryDao industryDao() {
        return new JAsyncIndustryDao(
            dictionaryConnectionPool(),
            env.getRequiredProperty("jdbc.dictionary.db.schema")
        );
    }
}

