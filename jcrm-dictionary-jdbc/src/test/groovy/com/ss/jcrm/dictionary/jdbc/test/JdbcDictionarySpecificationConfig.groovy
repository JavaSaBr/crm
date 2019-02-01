package com.ss.jcrm.dictionary.jdbc.test

import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig
import com.ss.jcrm.dictionary.jdbc.test.helper.DictionaryTestHelper
import com.ss.jcrm.integration.test.db.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.DbSpecificationUtils
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
        return DbSpecificationUtils.newDataSource(
            postgreSQLContainer,
            env.getRequiredProperty("jdbc.dictionary.db.schema")
        )
    }

    @Bean
    @NotNull DictionaryTestHelper dictionaryTestHelper() {
        return new DictionaryTestHelper(dictionaryDataSource(), countryDao)
    }
}
