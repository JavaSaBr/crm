package com.ss.jcrm.dictionary.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jasync.config.JAsyncDictionaryConfig;
import com.ss.jcrm.dictionary.web.handler.CountryHandler;
import com.ss.jcrm.dictionary.web.resource.CountryOutResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.dictionary.web.service.impl.DefaultCachedDictionaryService;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Import({
    JAsyncDictionaryConfig.class,
    WebSecurityConfig.class,
    BaseWebConfig.class
})
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/dictionary/web/dictionary-web.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/dictionary/web/dictionary-web-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class DictionaryWebConfig {

    private final @NotNull List<? extends Flyway> flyways;

    @Bean
    @NotNull CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService(
        @NotNull CountryDao countryDao,
        @NotNull ScheduledExecutorService mainScheduler,
        @NotNull Environment env
    ) {

        var service = new DefaultCachedDictionaryService<>(
            countryDao,
            CountryOutResource::new,
            objects -> objects.toArray(CountryOutResource[]::new)
        );
        service.reload();

        var interval = env.getProperty(
            "dictionary.web.cache.reload.interval",
            int.class,
            600
        );

        mainScheduler.scheduleAtFixedRate(service::reloadAsync, interval, interval, TimeUnit.SECONDS);

        return service;
    }

    @Bean
    @NotNull ApiEndpointServer dictionaryApiEndpointServer() {
        return new ApiEndpointServer("/dictionary");
    }

    @Bean
    @NotNull CountryHandler countryHandler(
        @NotNull CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService
    ) {
        return new CountryHandler(countryDictionaryService);
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> dictionaryStatusRouterFunction(
        @NotNull ApiEndpointServer dictionaryApiEndpointServer
    ) {
        var contextPath = dictionaryApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("${contextPath}/status", request -> ServerResponse.ok().build())
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> countryRouterFunction(
        @NotNull ApiEndpointServer dictionaryApiEndpointServer,
        @NotNull CountryHandler countryHandler
    ) {
        var contextPath = dictionaryApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("${contextPath}/countries", countryHandler::getAll)
            .GET("${contextPath}/country/id/{id}", countryHandler::getById)
            .GET("${contextPath}/country/name/{name}", countryHandler::getByName)
            .build();
    }
}
