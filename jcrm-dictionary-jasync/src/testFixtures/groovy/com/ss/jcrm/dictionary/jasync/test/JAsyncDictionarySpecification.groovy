package com.ss.jcrm.dictionary.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import crm.dictionary.api.DictionaryDbTestHelper
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JAsyncDictionarySpecificationConfig)
class JAsyncDictionarySpecification extends DefaultSpecification {

    static final String TABLE_CITY = "city"
    static final String TABLE_COUNTRY = "country"
    static final String TABLE_INDUSTRY = "industry"

    def static clearAllTables(
        ConnectionPool<? extends ConcreteConnection> connectionPool,
        String schema
    ) {
        DbSpecificationUtils.clearTable(connectionPool, schema, TABLE_CITY, TABLE_COUNTRY, TABLE_INDUSTRY)
    }

    @Autowired
    DictionaryDbTestHelper dictionaryTestHelper
    
    @Autowired
    List<? extends Flyway> flyways
    
    def setup() {
        dictionaryTestHelper.clearAllData()
    }
}
