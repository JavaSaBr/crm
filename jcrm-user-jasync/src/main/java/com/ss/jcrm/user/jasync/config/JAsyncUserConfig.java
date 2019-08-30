package com.ss.jcrm.user.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.jasync.config.JAsyncConfig;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.jasync.dao.JAsyncEmailConfirmationDao;
import com.ss.jcrm.user.jasync.dao.JAsyncOrganizationDao;
import com.ss.jcrm.user.jasync.dao.JAsyncUserDao;
import com.ss.jcrm.user.jasync.dao.JAsyncUserGroupDao;
import io.netty.channel.EventLoopGroup;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;

@Configuration
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/user/jasync/user-jasync.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/user/jasync/user-jasync-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@Import(JAsyncConfig.class)
public class JAsyncUserConfig {

    @Autowired
    private Environment env;

    @Autowired
    private PoolConfiguration dbPoolConfiguration;

    @Autowired
    private EventLoopGroup dbEventLoopGroup;

    @Autowired
    private ExecutorService dbExecutor;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private IndustryDao industryDao;

    @Autowired
    private CountryDao countryDao;

    @Bean
    @DependsOn("userConnectionPool")
    @NotNull Flyway userFlyway() {

        var flyway = Flyway.configure()
            .locations("classpath:com/ss/jcrm/user/db/migration")
            .baselineOnMigrate(true)
            .schemas(env.getRequiredProperty("jdbc.user.db.schema"))
            .dataSource(
                env.getRequiredProperty("jdbc.user.db.url"),
                env.getRequiredProperty("jdbc.user.db.username"),
                env.getRequiredProperty("jdbc.user.db.password")
            )
            .load();

        if (env.getProperty("db.upgrading.enabled", boolean.class, false)) {
            flyway.migrate();
        }

        return flyway;
    }

    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool() {

        var configuration = JAsyncUtils.buildConfiguration(
            env.getRequiredProperty("jdbc.user.db.username"),
            env.getRequiredProperty("jdbc.user.db.password"),
            env.getRequiredProperty("jdbc.user.db.host"),
            env.getRequiredProperty("jdbc.user.db.port", int.class),
            env.getRequiredProperty("jdbc.user.db.database")
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
    @NotNull UserDao userDao() {
        return new JAsyncUserDao(
            userConnectionPool(),
            env.getRequiredProperty("jdbc.user.db.schema"),
            organizationDao(),
            userGroupDao()
        );
    }

    @Bean
    @NotNull UserGroupDao userGroupDao() {
        return new JAsyncUserGroupDao(
            userConnectionPool(),
            env.getRequiredProperty("jdbc.user.db.schema"),
            organizationDao()
        );
    }

    @Bean
    @NotNull OrganizationDao organizationDao() {
        return new JAsyncOrganizationDao(
            userConnectionPool(),
            env.getRequiredProperty("jdbc.user.db.schema"),
            cityDao,
            industryDao,
            countryDao
        );
    }

    @Bean
    @NotNull EmailConfirmationDao emailConfirmationDao() {
        return new JAsyncEmailConfirmationDao(
            userConnectionPool(),
            env.getRequiredProperty("jdbc.user.db.schema")
        );
    }
}
