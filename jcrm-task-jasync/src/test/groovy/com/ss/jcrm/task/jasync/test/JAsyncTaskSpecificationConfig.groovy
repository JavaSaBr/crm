package com.ss.jcrm.task.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import com.ss.jcrm.task.api.dao.SimpleContactDao
import com.ss.jcrm.task.api.test.TaskTestHelper
import com.ss.jcrm.task.jasync.config.JAsyncTaskConfig
import com.ss.jcrm.task.jasync.test.helper.JAsyncTaskTestHelper
import com.ss.jcrm.user.api.test.UserTestHelper
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecificationConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@Import([
    JAsyncTaskConfig,
    JAsyncUserSpecificationConfig,
])
@PropertySource("classpath:com/ss/jcrm/task/jasync/test/task-jasync-test.properties")
class JAsyncTaskSpecificationConfig {
    
    @Autowired
    PostgreSQLContainer postgreSQLContainer
    
    @Autowired @Lazy
    UserTestHelper userTestHelper
    
    @Autowired @Lazy
    SimpleContactDao simpleContactDao
    
    @Autowired
    Environment env
    
    @Bean
    ConnectionPool<? extends ConcreteConnection> taskConnectionPool() {
        
        System.setProperty("jdbc.task.db.url", System.getProperty("db.test.url"))
        System.setProperty("jdbc.task.db.username", System.getProperty("db.test.username"))
        System.setProperty("jdbc.task.db.password", System.getProperty("db.test.password"))
        
        return DbSpecificationUtils.newConnectionPool(
            postgreSQLContainer,
            DbSpecificationConfig.DB_NAME
        )
    }
    
    @Bean
    TaskTestHelper taskTestHelper() {
        return new JAsyncTaskTestHelper(userTestHelper, simpleContactDao)
    }
}
