package com.ss.jcrm.task.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import com.ss.jcrm.task.api.test.TaskTestHelper
import com.ss.jcrm.user.api.test.UserTestHelper
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecificationConfig
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JdbcUserSpecificationConfig)
class JAsyncTaskSpecification extends DefaultSpecification {
    
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
    TaskTestHelper taskTestHelper
    
    @Autowired
    List<? extends Flyway> flyways
    
    def setup() {
        userTestHelper.clearAllData()
    }
}
