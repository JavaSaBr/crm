package com.ss.jcrm.client.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.client.api.dao.SimpleContactDao
import com.ss.jcrm.client.api.test.ClientTestHelper
import com.ss.jcrm.client.jasync.config.JAsyncClientConfig
import com.ss.jcrm.client.jasync.test.helper.JAsyncClientTestHelper
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
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
    JAsyncClientConfig,
    JAsyncUserSpecificationConfig,
])
@PropertySource("classpath:com/ss/jcrm/client/jasync/test/client-jasync-test.properties")
class JAsyncClientSpecificationConfig {
    
    @Autowired
    PostgreSQLContainer postgreSQLContainer
    
    @Autowired @Lazy
    UserTestHelper userTestHelper
    
    @Autowired @Lazy
    SimpleContactDao simpleContactDao
    
    @Autowired
    Environment env
    
    @Bean
    ConnectionPool<? extends ConcreteConnection> clientConnectionPool() {
        
        System.setProperty("jdbc.client.db.url", System.getProperty("db.test.url"))
        System.setProperty("jdbc.client.db.username", System.getProperty("db.test.username"))
        System.setProperty("jdbc.client.db.password", System.getProperty("db.test.password"))
        
        return DbSpecificationUtils.newConnectionPool(
            postgreSQLContainer,
            DbSpecificationConfig.DB_NAME
        )
    }
    
    @Bean
    ClientTestHelper taskTestHelper() {
        return new JAsyncClientTestHelper(
            clientConnectionPool(),
            env.getRequiredProperty("jdbc.client.db.schema"),
            userTestHelper,
            simpleContactDao
        )
    }
}
