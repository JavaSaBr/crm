package com.ss.jcrm.dictionary.jasync.test

import com.ss.jcrm.dictionary.jdbc.test.helper.JdbcDictionaryTestHelper
import com.ss.jcrm.integration.test.DefaultSpecification
import org.flywaydb.core.Flyway
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import javax.sql.DataSource

@ContextConfiguration(classes = JdbcDictionarySpecificationConfig)
class JAsyncDictionarySpecification extends DefaultSpecification {

    static final String TABLE_CITY = "city"
    static final String TABLE_COUNTRY = "country"
    static final String TABLE_INDUSTRY = "industry"

    def static clearAllTables(@NotNull DataSource dictionaryDataSource) {
        com.ss.jcrm.integration.test.db.DbSpecificationUtils.clearTable(dictionaryDataSource, TABLE_CITY, TABLE_COUNTRY, TABLE_INDUSTRY)
    }

    @Autowired
    JdbcDictionaryTestHelper dictionaryTestHelper
    
    @Autowired
    List<? extends Flyway> flyways
    
    def setup() {
        dictionaryTestHelper.clearAllData()
    }
}
