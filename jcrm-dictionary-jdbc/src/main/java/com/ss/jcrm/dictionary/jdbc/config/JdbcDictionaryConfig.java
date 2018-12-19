package com.ss.jcrm.dictionary.jdbc.config;

import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.DictionaryDao;
import com.ss.jcrm.jdbc.ConnectionPoolFactory;
import com.ss.jcrm.jdbc.config.JdbcConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@Import(JdbcConfig.class)
public class JdbcDictionaryConfig {

    @Autowired
    private Executor fastDbTaskExecutor;

    @Autowired
    private Executor slowDbTaskExecutor;

    @Autowired
    Environment env;

    @Bean
    @NotNull DataSource dictionaryDataSource() {
        return ConnectionPoolFactory.newDataSource(
            env.getRequiredProperty("jdbc.dictionary.db.url"),
            env.getRequiredProperty("jdbc.dictionary.db.schema"),
            env.getRequiredProperty("jdbc.dictionary.db.username"),
            env.getRequiredProperty("jdbc.dictionary.db.password")
        );
    }

    @Bean
    @NotNull DictionaryDao<Country> countryDao() {
        return new JdbcUserRoleDao(userDataSource(), fastDbTaskExecutor, slowDbTaskExecutor);
    }
}
