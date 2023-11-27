package crm.dao;

import crm.base.spring.config.BaseConfig;
import crm.dao.migration.DbMigrationFactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import crm.dao.config.DaoConfig;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

@Import({
    BaseConfig.class,
    DaoConfig.class
})
@Configuration(proxyBeanMethods = false)
public class FlywaysConfig {

  @Bean
  Void initFlyways(List<DbMigrationFactory<Flyway>> flywayFactories, ExecutorService scalableExecutor) {

    var migrateFutures = flywayFactories
        .stream()
        .filter(factory -> factory.type().equals(Flyway.class))
        .map(DbMigrationFactory::build)
        .map(flyway -> CompletableFuture.runAsync(flyway::migrate, scalableExecutor))
        .toList();

    migrateFutures.forEach(CompletableFuture::join);

    return null;
  }

  @DependsOn("initFlyways")
  @Bean(DaoConfig.BEAN_DB_READY)
  Void dbReady() {
    return null;
  }
}
