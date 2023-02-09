package jcrm.dao;

import crm.base.spring.config.BaseConfig;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import jcrm.dao.config.DaoConfig;
import org.flywaydb.core.Flyway;
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
  Void initFlyways(List<Flyway> flyways, ExecutorService scalableExecutor) {

    var migrateFutures = flyways
        .stream()
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
