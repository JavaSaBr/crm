package com.ss.jcrm.dictionary.jdbc.test

import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig
import com.ss.jcrm.dictionary.jdbc.test.helper.JdbcDictionaryTestHelper
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.jdbc.util.DbSpecificationUtils
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource

@Configuration
@Import([
    JdbcDictionaryConfig,
    DbSpecificationConfig,
])
@PropertySource("classpath:com/ss/jcrm/dictionary/jdbc/test/dictionary-jdbc-test.properties")
class JdbcDictionarySpecificationConfig {

    @Autowired
    PostgreSQLContainer postgreSQLContainer

    @Autowired
    Environment env

    @Autowired @Lazy
    CountryDao countryDao
    
    @Bean
    @NotNull DataSource dictionaryDataSource() {
        
        System.setProperty("jdbc.dictionary.db.url", System.getProperty("db.test.url"))
        System.setProperty("jdbc.dictionary.db.username", System.getProperty("db.test.username"))
        System.setProperty("jdbc.dictionary.db.password", System.getProperty("db.test.password"))
        
        return DbSpecificationUtils.newDataSource(
            postgreSQLContainer,
            env.getRequiredProperty("jdbc.dictionary.db.schema")
        )
    }

    @Bean
    @NotNull DictionaryTestHelper dictionaryTestHelper() {
        return new JdbcDictionaryTestHelper(dictionaryDataSource(), countryDao)
    }
}
