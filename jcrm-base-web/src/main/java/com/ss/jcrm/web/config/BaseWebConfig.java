package com.ss.jcrm.web.config;

import com.ss.jcrm.web.converter.JsoniterHttpMessageConverter;
import com.ss.rlib.common.concurrent.GroupThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Import({
    WebFluxAutoConfiguration.class,
    HttpHandlerAutoConfiguration.class,
    ReactiveWebServerFactoryAutoConfiguration.class,
})
@Configuration
public class BaseWebConfig {

    @Autowired
    private Environment env;
/*
    @Override
    protected void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new JsoniterHttpMessageConverter());
    }*/

    @Bean
    @NotNull HttpMessageConverter<?> jsoniterHttpMessageConverter() {
        return new JsoniterHttpMessageConverter();
    }

    @Bean
    @NotNull Executor controllerExecutor() {

        var cores = Runtime.getRuntime().availableProcessors() * 2;

        return Executors.newFixedThreadPool(
            env.getProperty("controller.executor.threads", Integer.class, cores),
            new GroupThreadFactory("ControllerThread")
        );
    }

    @Bean
    @NotNull ScheduledExecutorService reloadScheduler() {

        var cores = Runtime.getRuntime().availableProcessors() * 2;

        return Executors.newScheduledThreadPool(
            env.getProperty("reloading.executor.threads", Integer.class, cores),
            new GroupThreadFactory("ReloadingThread")
        );
    }
}
