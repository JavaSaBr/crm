package com.ss.jcrm.web.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ss.jcrm.web.customizer.UndertowWebServerFactorySslCustomizer;
import com.ss.jcrm.web.exception.handler.DefaultWebExceptionHandler;
import com.ss.rlib.common.concurrent.GroupThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:com/ss/jcrm/web/base-web.properties")
public class BaseWebConfig {

    @Bean
    @NotNull Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.modules(
            new BlackbirdModule(),
            new ParameterNamesModule(),
            new JavaTimeModule()
        );
    }

    @Bean
    @NotNull ScheduledExecutorService mainScheduler(@NotNull Environment env) {
        return Executors.newScheduledThreadPool(
            env.getProperty("main.scheduler.threads", int.class, 1),
            new GroupThreadFactory("main-scheduler-thread")
        );
    }

    @Bean
    @NotNull WebServerFactoryCustomizer<UndertowReactiveWebServerFactory> undertowWebServerFactoryCustomizer(
        @NotNull Environment env
    ) {
        return new UndertowWebServerFactorySslCustomizer(
            env.getRequiredProperty("web.server.ssl.keystore.path"),
            env.getRequiredProperty("web.server.ssl.keystore.password"),
            env.getRequiredProperty("web.server.ssl.key.alias")
        );
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
