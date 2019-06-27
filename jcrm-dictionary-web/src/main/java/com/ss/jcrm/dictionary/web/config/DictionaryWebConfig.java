package com.ss.jcrm.dictionary.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.dictionary.web.handler.CountryHandler;
import com.ss.jcrm.dictionary.web.resource.AllCountriesOutResource;
import com.ss.jcrm.dictionary.web.resource.CountryOutResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.dictionary.web.service.impl.DefaultCachedDictionaryService;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Import({
    JdbcDictionaryConfig.class,
    WebSecurityConfig.class,
    BaseWebConfig.class
})
@Configuration
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/dictionary/web/dictionary-web.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/dictionary/web/dictionary-web-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DictionaryWebConfig {

    @Autowired
    private final Environment env;

    @Autowired
    private List<? extends Flyway> flyways;

    @Bean
    @NotNull CachedDictionaryService<CountryOutResource, AllCountriesOutResource> countryDictionaryService(
        @NotNull CountryDao countryDao,
        @NotNull ScheduledExecutorService reloadScheduler
    ) {

        var service = new DefaultCachedDictionaryService<>(
            countryDao,
            CountryOutResource::new,
            AllCountriesOutResource::new
        );
        service.reload();

        int interval = env.getProperty("dictionary.web.cache.reload.interval", int.class, 600);

        reloadScheduler.scheduleAtFixedRate(service::reloadAsync, interval, interval, TimeUnit.SECONDS);

        return service;
    }

    @Bean
    @NotNull ApiEndpointServer dictionaryApiEndpointServer() {
        return new ApiEndpointServer("/dictionary");
    }

    @Bean
    @NotNull CountryHandler countryHandler(
        @NotNull CachedDictionaryService<CountryOutResource, AllCountriesOutResource> countryDictionaryService
    ) {
        return new CountryHandler(countryDictionaryService);
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> dictionaryStatusRouterFunction() {
        return RouterFunctions.route()
            .GET("/dictionary/status", request -> ServerResponse.ok()
                .build())
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> countryRouterFunction(@NotNull CountryHandler countryHandler) {
        return RouterFunctions.route()
            .GET("/dictionary/countries", countryHandler::getAll)
            .GET("/dictionary/country/{id}", countryHandler::getById)
            .GET("/dictionary/name/{name}", countryHandler::getByName)
            .build();
    }
}
