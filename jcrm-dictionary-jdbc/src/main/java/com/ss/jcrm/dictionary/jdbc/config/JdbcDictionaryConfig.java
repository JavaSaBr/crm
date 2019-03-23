package com.ss.jcrm.dictionary.jdbc.config;

import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.jdbc.dao.JdbcCityDao;
import com.ss.jcrm.dictionary.jdbc.dao.JdbcCountryDao;
import com.ss.jcrm.dictionary.jdbc.dao.JdbcIndustryDao;
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
    private Environment env;

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
    @NotNull CountryDao countryDao() {
        return new JdbcCountryDao(dictionaryDataSource(), fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Bean
    @NotNull CityDao cityDao() {
        return new JdbcCityDao(dictionaryDataSource(), countryDao(), fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Bean
    @NotNull IndustryDao industryDao() {
        return new JdbcIndustryDao(dictionaryDataSource(), fastDbTaskExecutor, slowDbTaskExecutor);
    }
}

