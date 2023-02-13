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
import crm.jasync.config.CommonJAsyncConfig;
import crm.jasync.util.JAsyncUtils;
import io.netty.channel.EventLoopGroup;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;

@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/dictionary/jasync/dictionary-jasync.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/dictionary/jasync/dictionary-jasync-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@Import(CommonJAsyncConfig.class)
@Configuration(proxyBeanMethods = false)
public class JAsyncDictionaryConfig {

    @Bean
    @DependsOn("dictionaryConnectionPool")
    @NotNull Flyway dictionaryFlyway(@NotNull Environment env) {

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
    @NotNull String dictionaryDbSchema(@NotNull Environment env) {
        return env.getRequiredProperty("jdbc.dictionary.db.schema");
    }

    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool(
        @NotNull Environment env,
        @NotNull EventLoopGroup dbEventLoopGroup,
        @NotNull PoolConfiguration dbPoolConfiguration,
        @NotNull ExecutorService dbExecutor
    ) {

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
    @NotNull CountryDao countryDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool,
        @NotNull String dictionaryDbSchema
    ) {
        return new JAsyncCountryDao(
            dictionaryConnectionPool,
            dictionaryDbSchema
        );
    }

    @Bean
    @NotNull CityDao cityDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool,
        @NotNull String dictionaryDbSchema,
        @NotNull CountryDao countryDao
    ) {
        return new JAsyncCityDao(
            dictionaryConnectionPool,
            dictionaryDbSchema,
            countryDao
        );
    }

    @Bean
    @NotNull IndustryDao industryDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool,
        @NotNull String dictionaryDbSchema
    ) {
        return new JAsyncIndustryDao(
            dictionaryConnectionPool,
            dictionaryDbSchema
        );
    }
}

