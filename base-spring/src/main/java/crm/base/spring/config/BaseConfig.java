package crm.base.spring.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class BaseConfig {

  @Bean
  @NotNull ExecutorService scalableExecutor() {
    return Executors.newCachedThreadPool();
  }
}
