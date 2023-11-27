package crm.client.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.base.spring.config.PropertiesConfig;
import crm.client.api.dao.SimpleClientDao;
import crm.client.jasync.dao.JAsyncSimpleClientDao;
import crm.dao.migration.DbMigrationFactory;
import crm.jasync.config.AbstractJAsyncConfig;
import crm.jasync.config.CommonJAsyncConfig;
import crm.dao.config.DaoConfig;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
    @PropertySource("classpath:client/jasync/client-jasync.properties"),
    @PropertySource(value = "classpath:client/jasync/client-jasync-${spring.profiles.active:default}.properties", ignoreResourceNotFound = true)
})
@Import(CommonJAsyncConfig.class)
@Configuration(proxyBeanMethods = false)
public class JAsyncClientConfig extends AbstractJAsyncConfig {

  public JAsyncClientConfig(@NotNull ApplicationContext applicationContext) {
    super(applicationContext, "client.db");
  }

  @Bean
  @DependsOn(PropertiesConfig.BEAN_FINAL_PROPS)
  @NotNull DbMigrationFactory<Flyway> clientFlyway() {
    return new DbMigrationFactory<>(Flyway.class, this::flyway);
  }

  @Bean
  @DependsOn({
      PropertiesConfig.BEAN_FINAL_PROPS,
      DaoConfig.BEAN_DB_READY
  })
  @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool() {
    return connectionPool();
  }

  @Bean
  @NotNull SimpleClientDao simpleContactDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool) {
    return new JAsyncSimpleClientDao(clientConnectionPool, schema());
  }
}

