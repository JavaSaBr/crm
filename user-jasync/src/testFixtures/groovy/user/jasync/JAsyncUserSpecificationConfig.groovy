package user.jasync

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils
import com.ss.jcrm.security.config.SecurityConfig
import com.ss.jcrm.security.service.PasswordService
import crm.user.api.dao.EmailConfirmationDao
import crm.user.api.dao.OrganizationDao
import crm.user.api.dao.UserDao
import crm.user.api.dao.UserGroupDao
import crm.user.api.test.UserTestHelper
import user.jasync.config.JAsyncUserConfig
import user.jasync.helper.JAsyncUserTestHelper
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@Import([
    JAsyncUserConfig,
    DbSpecificationConfig,
    JAsyncDictionarySpecificationConfig,
    SecurityConfig
])
@PropertySource("classpath:user/jasync/user-jasync-test.properties")
class JAsyncUserSpecificationConfig {
  
  @Autowired
  PostgreSQLContainer postgreSQLContainer
  
  @Autowired
  Environment env
  
  @Autowired
  PasswordService passwordService
  
  @Autowired @Lazy
  UserDao userDao
  
  @Autowired @Lazy
  OrganizationDao organizationDao
  
  @Autowired @Lazy
  UserGroupDao userRoleDao
  
  @Autowired @Lazy
  EmailConfirmationDao emailConfirmationDao
  
  @Autowired
  DictionaryTestHelper dictionaryTestHelper
  
  @Bean
  ConnectionPool<? extends ConcreteConnection> userConnectionPool() {
    
    System.setProperty("user.db.url", System.getProperty("db.test.url"))
    System.setProperty("user.db.username", System.getProperty("db.test.username"))
    System.setProperty("user.db.password", System.getProperty("db.test.password"))
    
    return DbSpecificationUtils.newConnectionPool(
        postgreSQLContainer,
        DbSpecificationConfig.dbName)
  }
  
  @Bean
  @NotNull
  UserTestHelper userTestHelper() {
    return new JAsyncUserTestHelper(
        userConnectionPool(),
        env.getRequiredProperty("jdbc.user.db.schema"),
        userDao,
        userRoleDao,
        organizationDao,
        passwordService,
        dictionaryTestHelper,
        emailConfirmationDao)
  }
}
