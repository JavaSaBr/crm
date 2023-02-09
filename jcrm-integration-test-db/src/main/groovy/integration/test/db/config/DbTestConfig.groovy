package integration.test.db.config

import com.ss.jcrm.integration.test.config.BaseTestConfig
import com.ss.jcrm.integration.test.model.PropertiesTestProvider
import integration.test.db.model.DbTestPrefix
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT

@Import([
    BaseTestConfig
])
@Configuration(proxyBeanMethods = false)
class DbTestConfig {
  
  static final String dbName = "test-db"
  static final String dbUser = "test-root"
  static final String dbPassword = "test-root"
  
  @Bean(destroyMethod = "stop")
  PostgreSQLContainer postgreSqlContainer() {
    
    def container = new PostgreSQLContainer("postgres:12")
        .withDatabaseName(dbName)
        .withUsername(dbUser)
        .withPassword(dbPassword)
    
    container.start()
    
    while (!container.isRunning()) {
      Thread.sleep(500)
    }
    
    def mappedPort = container.getMappedPort(POSTGRESQL_PORT)
    
    System.setProperty("db.test.url", "jdbc:postgresql://${container.getContainerIpAddress()}:${mappedPort}/$dbName")
    System.setProperty("db.test.username", dbUser)
    System.setProperty("db.test.password", dbPassword)
    
    return container
  }
  
  @Bean
  PropertiesTestProvider dbTestPropertiesProvider(
      @NotNull List<DbTestPrefix> prefixes,
      @NotNull PostgreSQLContainer postgreSqlContainer) {
  
    if (prefixes.isEmpty()) {
      throw new IllegalStateException("No any DB test prefix")
    }
    
    def mappedPort = postgreSqlContainer.getMappedPort(POSTGRESQL_PORT)
    def host = postgreSqlContainer.host
    
    return {
    
      def result = [:]
  
      prefixes.forEach {
        result["${it.propertyPrefix}.url"] = "jdbc:postgresql://$host:$mappedPort/$dbName"
        result["${it.propertyPrefix}.host"] = host
        result["${it.propertyPrefix}.port"] = "$mappedPort"
        result["${it.propertyPrefix}.database"] = dbName
        result["${it.propertyPrefix}.schema"] = "${it.schemaPrefix}_${it.dbTestSchema.name().toLowerCase()}"
        result["${it.propertyPrefix}.username"] = dbUser
        result["${it.propertyPrefix}.password"] = dbPassword
      }
  
      return result.collectEntries {
        key, value -> [key.toString(), value.toString()]
      }
    }
  }
}
