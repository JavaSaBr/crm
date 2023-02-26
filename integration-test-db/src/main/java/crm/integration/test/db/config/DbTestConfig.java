package crm.integration.test.db.config;

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT;

import crm.integration.test.config.BaseTestConfig;
import crm.integration.test.db.model.DbTestPrefix;
import crm.integration.test.model.PropertiesTestProvider;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Import({
    BaseTestConfig.class
})
@Configuration(proxyBeanMethods = false)
public class DbTestConfig {

  public static final String dbName = "test-db";
  public static final String dbUser = "test-root";
  public static final String dbPassword = "test-root";

  @Bean(destroyMethod = "stop")
  PostgreSQLContainer postgreSqlContainer() {

    var container = new PostgreSQLContainer("postgres:12")
        .withDatabaseName(dbName)
        .withUsername(dbUser)
        .withPassword(dbPassword);

    container.start();

    while (!container.isRunning()) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    var mappedPort = container.getMappedPort(POSTGRESQL_PORT);
    var host = container.getContainerIpAddress();

    System.setProperty("db.test.url", "jdbc:postgresql://%s:%d/%s".formatted(host, mappedPort, dbName));
    System.setProperty("db.test.username", dbUser);
    System.setProperty("db.test.password", dbPassword);

    return container;
  }

  @Bean
  PropertiesTestProvider dbTestPropertiesProvider(
      @NotNull List<DbTestPrefix> prefixes,
      @NotNull PostgreSQLContainer postgreSqlContainer) {

    if (prefixes.isEmpty()) {
      throw new IllegalStateException("No any DB test prefix");
    }

    var mappedPort = postgreSqlContainer.getMappedPort(POSTGRESQL_PORT);
    var host = postgreSqlContainer.getHost();

    return () -> {

      var result = new HashMap<String, Object>();

      prefixes.forEach(prefix -> {

        var finalSchema = prefix.schemaPrefix() + "_" + prefix
            .dbTestSchema()
            .name()
            .toLowerCase();

        result.put(prefix.propertyPrefix() + ".url", "jdbc:postgresql://%s:%d/%s".formatted(host, mappedPort, dbName));
        result.put(prefix.propertyPrefix() + ".host", host);
        result.put(prefix.propertyPrefix() + ".port", String.valueOf(mappedPort));
        result.put(prefix.propertyPrefix() + ".database", dbName);
        result.put(prefix.propertyPrefix() + ".schema", finalSchema);
        result.put(prefix.propertyPrefix() + ".database", dbName);
        result.put(prefix.propertyPrefix() + ".username", dbUser);
        result.put(prefix.propertyPrefix() + ".password", dbPassword);
      });

      return result;
    };
  }
}
