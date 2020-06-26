package com.ss.jcrm.web.config;

import com.ss.jcrm.web.customizer.UndertowWebServerFactorySslCustomizer;
import com.ss.jcrm.web.exception.handler.DefaultWebExceptionHandler;
import com.ss.rlib.common.concurrent.GroupThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebExceptionHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Lazy
@Import({
    WebFluxAutoConfiguration.class,
    HttpHandlerAutoConfiguration.class,
    ReactiveWebServerFactoryAutoConfiguration.class
})
@Configuration
@PropertySource("classpath:com/ss/jcrm/web/base-web.properties")
public class BaseWebConfig {

    /*@Bean
    @NotNull WebFluxConfigurer webFluxConfigurer() {
        return new WebFluxConfigurer() {

            @Override
            public void configureHttpMessageCodecs(@NotNull ServerCodecConfigurer configurer) {
                configurer.defaultCodecs().jackson2JsonEncoder(new JsoniterHttpMessageEncoder());
            }
        };
    }*/

    @Bean
    @NotNull ScheduledExecutorService reloadScheduler(@NotNull Environment env) {

        var cores = Runtime.getRuntime().availableProcessors() * 2;

        return Executors.newScheduledThreadPool(
            env.getProperty("reloading.executor.threads", int.class, cores),
            new GroupThreadFactory("reloading-thread")
        );
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
}
