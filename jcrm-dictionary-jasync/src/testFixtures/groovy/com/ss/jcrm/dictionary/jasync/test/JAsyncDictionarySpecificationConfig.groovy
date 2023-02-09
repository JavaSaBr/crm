package com.ss.jcrm.dictionary.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.api.test.DictionaryDbTestHelper
import com.ss.jcrm.dictionary.jasync.config.JAsyncDictionaryConfig
import com.ss.jcrm.dictionary.jasync.test.helper.JAsyncDictionaryDbTestHelper
import integration.test.db.config.DbTestConfig
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@Import([
    JAsyncDictionaryConfig,
    DbTestConfig,
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
    ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool() {
    
        System.setProperty("jdbc.dictionary.db.url", System.getProperty("db.test.url"))
        System.setProperty("jdbc.dictionary.db.username", System.getProperty("db.test.username"))
        System.setProperty("jdbc.dictionary.db.password", System.getProperty("db.test.password"))
        
        return DbSpecificationUtils.newConnectionPool(
            postgreSQLContainer,
            DbTestConfig.dbName
        )
    }
    
    @Bean
    DictionaryDbTestHelper dictionaryTestHelper() {
        return new JAsyncDictionaryDbTestHelper(
            dictionaryConnectionPool(),
            countryDao,
            env.getRequiredProperty("jdbc.dictionary.db.schema")
        )
    }
}
