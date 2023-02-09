package crm.base.web.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import crm.base.web.customizer.UndertowWebServerFactorySslCustomizer;
import crm.base.web.exception.handler.DefaultWebExceptionHandler;
import com.ss.rlib.common.concurrent.GroupThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.web.server.WebExceptionHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Lazy
@Import({
    WebFluxAutoConfiguration.class,
    HttpHandlerAutoConfiguration.class,
    ReactiveWebServerFactoryAutoConfiguration.class
})
@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:crm/base/web/base-web.properties")
public class BaseWebConfig {

  @Bean
  @NotNull Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> builder.modules(
        new BlackbirdModule(),
        new ParameterNamesModule(),
        new JavaTimeModule());
  }

  @Bean
  @NotNull ScheduledExecutorService mainScheduler(@Value("${main.scheduler.threads:1}") int threads) {
    return Executors.newScheduledThreadPool(threads, new GroupThreadFactory("main-scheduler-thread"));
  }

  @Bean
  @NotNull WebServerFactoryCustomizer<UndertowReactiveWebServerFactory> undertowWebServerFactoryCustomizer(
      @Value("${web.server.ssl.keystore.path}") @NotNull String keystore,
      @Value("${web.server.ssl.keystore.password}") @NotNull String password,
      @Value("${web.server.ssl.key.alias}") @NotNull String alias) {
    return new UndertowWebServerFactorySslCustomizer(keystore, password, alias);
  }

  @Bean
  @NotNull WebExceptionHandler defaultWebExceptionHandler() {
    return new DefaultWebExceptionHandler();
  }

    /* @Lazy
     @Bean
     @NotNull WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyWebServerFactoryCustomizer() {
         return new NettyWebServerFactorySslCustomizer(
             env.getRequiredProperty("web.server.ssl.keystore.path"),
             env.getRequiredProperty("web.server.ssl.keystore.password"),
             env.getRequiredProperty("web.server.ssl.key.alias")
         );
     }
 */
}
