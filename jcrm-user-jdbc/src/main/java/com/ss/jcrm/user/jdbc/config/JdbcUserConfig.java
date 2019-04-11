package com.ss.jcrm.user.jdbc.config;

import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.jdbc.ConnectionPoolFactory;
import com.ss.jcrm.jdbc.config.JdbcConfig;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.jdbc.dao.JdbcEmailConfirmationDao;
import com.ss.jcrm.user.jdbc.dao.JdbcOrganizationDao;
import com.ss.jcrm.user.jdbc.dao.JdbcUserDao;
import com.ss.jcrm.user.jdbc.dao.JdbcUserGroupDao;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@Import(JdbcConfig.class)
public class JdbcUserConfig {

    @Autowired
    private Executor fastDbTaskExecutor;

    @Autowired
    private Executor slowDbTaskExecutor;

    @Autowired
    private Environment env;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private IndustryDao industryDao;

    @Autowired
    private CountryDao countryDao;

    @Bean
    @DependsOn("userDataSource")
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
    @NotNull DataSource userDataSource() {
        return ConnectionPoolFactory.newDataSource(
            env.getRequiredProperty("jdbc.user.db.url"),
            env.getRequiredProperty("jdbc.user.db.schema"),
            env.getRequiredProperty("jdbc.user.db.username"),
            env.getRequiredProperty("jdbc.user.db.password")
        );
    }

    @Bean
    @NotNull UserDao userDao() {
        return new JdbcUserDao(
            userDataSource(),
            fastDbTaskExecutor,
            slowDbTaskExecutor,
            organizationDao(),
            userGroupDao()
        );
    }

    @Bean
    @NotNull UserGroupDao userGroupDao() {
        return new JdbcUserGroupDao(userDataSource(), fastDbTaskExecutor, slowDbTaskExecutor, organizationDao());
    }

    @Bean
    @NotNull OrganizationDao organizationDao() {
        return new JdbcOrganizationDao(
            userDataSource(),
            fastDbTaskExecutor,
            slowDbTaskExecutor,
            cityDao,
            industryDao,
            countryDao
        );
    }

    @Bean
    @NotNull EmailConfirmationDao emailConfirmationDao() {
        return new JdbcEmailConfirmationDao(userDataSource(), fastDbTaskExecutor, slowDbTaskExecutor);
    }

}
