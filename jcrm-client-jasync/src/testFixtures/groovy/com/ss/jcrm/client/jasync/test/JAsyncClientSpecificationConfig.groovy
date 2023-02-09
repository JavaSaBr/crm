package com.ss.jcrm.client.jasync.test

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.client.api.dao.SimpleClientDao
import com.ss.jcrm.client.api.test.ClientDbTestHelper
import com.ss.jcrm.client.jasync.config.JAsyncClientConfig
import com.ss.jcrm.client.jasync.test.helper.JAsyncClientDbTestHelper
import crm.user.api.UserDbTestHelper
import integration.test.db.config.DbTestConfig
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils

import crm.user.jasync.config.JAsyncUserTestConfig
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
    JAsyncUserTestConfig,
])
@PropertySource("classpath:com/ss/jcrm/client/jasync/test/client-jasync-test.properties")
class JAsyncClientSpecificationConfig {
  
  @Autowired
  PostgreSQLContainer postgreSQLContainer
  
  @Autowired
  @Lazy
  UserDbTestHelper userTestHelper
  
  @Autowired
  @Lazy
  SimpleClientDao simpleContactDao
  
  @Bean
  ConnectionPool<? extends ConcreteConnection> clientConnectionPool() {
    
    System.setProperty("jdbc.client.db.url", System.getProperty("db.test.url"))
    System.setProperty("jdbc.client.db.username", System.getProperty("db.test.username"))
    System.setProperty("jdbc.client.db.password", System.getProperty("db.test.password"))
    
    return DbSpecificationUtils.newConnectionPool(
        postgreSQLContainer,
        DbTestConfig.dbName
    )
  }
  
  @Bean
  ClientDbTestHelper taskTestHelper(Environment env) {
    return new JAsyncClientDbTestHelper(
        clientConnectionPool(),
        env.getRequiredProperty("jdbc.client.db.schema"),
        userTestHelper,
        simpleContactDao
    )
  }
}
