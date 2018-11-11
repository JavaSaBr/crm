package com.ss.jcrm.user.impl.jdbc;

import com.ss.jcrm.jdbc.ConnectionPoolFactory;
import com.ss.jcrm.jdbc.config.JdbcConfig;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserRoleDao;
import com.ss.jcrm.user.impl.jdbc.dao.JdbcOrganizationDao;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@Import(JdbcConfig.class)
public class JdbcUserConfig {

    @Autowired
    private Executor fastDbTaskExecutor;

    @Value("jdbc.user.db.url")
    private String url;

    @Value("jdbc.user.db.username")
    private String username;

    @Value("jdbc.user.db.password")
    private String password;

    @Bean @Lazy
    @NotNull Flyway userFlyway() {

        var flyway = Flyway.configure()
            .dataSource(url, username, password)
            .load();

        flyway.migrate();

        return flyway;
    }

    @Bean
    @NotNull DataSource userDataSource() {
        return ConnectionPoolFactory.newDataSource(url, username, password);
    }

    @Bean
    @NotNull UserDao userDao() {
        return null;
    }

    @Bean
    @NotNull UserRoleDao userRoleDao() {
        return null;
    }

    @Bean
    @NotNull OrganizationDao organizationDao() {
        return new JdbcOrganizationDao(userDataSource(), fastDbTaskExecutor);
    }
}
