package com.ss.jcrm.user.jdbc.config;

import com.ss.jcrm.jdbc.ConnectionPoolFactory;
import com.ss.jcrm.jdbc.config.JdbcConfig;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserRoleDao;
import com.ss.jcrm.user.jdbc.dao.JdbcOrganizationDao;
import com.ss.jcrm.user.jdbc.dao.JdbcUserDao;
import com.ss.jcrm.user.jdbc.dao.JdbcUserRoleDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@Import(JdbcConfig.class)
public class JdbcUserConfig {

    @Autowired
    private Executor fastDbTaskExecutor;

    @Autowired
    private Executor slowDbTaskExecutor;

    @Value("${jdbc.user.db.url}")
    private String url;

    @Value("${jdbc.user.db.schema}")
    private String schema;

    @Value("${jdbc.user.db.username}")
    private String username;

    @Value("${jdbc.user.db.password}")
    private String password;

    @Bean
    @NotNull DataSource userDataSource() {
        return ConnectionPoolFactory.newDataSource(url, schema, username, password);
    }

    @Bean
    @NotNull UserDao userDao() {
        return new JdbcUserDao(
            userDataSource(),
            fastDbTaskExecutor,
            slowDbTaskExecutor,
            organizationDao(),
            userRoleDao()
        );
    }

    @Bean
    @NotNull UserRoleDao userRoleDao() {
        return new JdbcUserRoleDao(userDataSource(), fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Bean
    @NotNull OrganizationDao organizationDao() {
        return new JdbcOrganizationDao(userDataSource(), fastDbTaskExecutor, slowDbTaskExecutor);
    }
}
