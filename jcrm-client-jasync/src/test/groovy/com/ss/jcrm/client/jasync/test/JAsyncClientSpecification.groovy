package com.ss.jcrm.client.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.client.api.test.ClientTestHelper
import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import com.ss.jcrm.user.api.test.UserTestHelper
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JAsyncClientSpecificationConfig)
class JAsyncClientSpecification extends DefaultSpecification {
    
    static final String TABLE_CONTACT = "contact"
    
    def static clearAllTables(
        ConnectionPool<? extends ConcreteConnection> connectionPool,
        String schema
    ) {
        DbSpecificationUtils.clearTable(connectionPool, schema, TABLE_CONTACT)
    }
    
    @Autowired
    UserTestHelper userTestHelper
    
    @Autowired
    ClientTestHelper clientTestHelper
    
    @Autowired
    List<? extends Flyway> flyways
    
    def setup() {
        userTestHelper.clearAllData()
    }
}
