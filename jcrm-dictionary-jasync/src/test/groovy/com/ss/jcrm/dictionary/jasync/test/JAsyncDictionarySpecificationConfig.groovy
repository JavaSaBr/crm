package com.ss.jcrm.dictionary.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.dictionary.jasync.config.JAsyncDictionaryConfig
import com.ss.jcrm.dictionary.jasync.test.helper.JAsyncDictionaryTestHelper
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@Import([
    JAsyncDictionaryConfig,
    DbSpecificationConfig,
])
@PropertySource("classpath:com/ss/jcrm/dictionary/jasync/test/dictionary-jasync-test.properties")
class JAsyncDictionarySpecificationConfig {

    @Autowired
    PostgreSQLContainer postgreSQLContainer

    @Autowired
    Environment env

    @Autowired @Lazy
    CountryDao countryDao
    
    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool() {
    
        System.setProperty("jdbc.dictionary.db.url", System.getProperty("db.test.url"))
        System.setProperty("jdbc.dictionary.db.username", System.getProperty("db.test.username"))
        System.setProperty("jdbc.dictionary.db.password", System.getProperty("db.test.password"))
        
        return DbSpecificationUtils.newConnectionPool(
            postgreSQLContainer,
            DbSpecificationConfig.DB_NAME
        )
    }
    
    @Bean
    @NotNull DictionaryTestHelper dictionaryTestHelper() {
        return new JAsyncDictionaryTestHelper(
            dictionaryConnectionPool(),
            countryDao,
            env.getRequiredProperty("jdbc.dictionary.db.schema")
        )
    }
}
