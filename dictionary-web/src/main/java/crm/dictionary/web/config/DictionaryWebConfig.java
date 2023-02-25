package crm.dictionary.web.config;

import crm.dictionary.api.dao.CountryDao;
import crm.dictionary.jasync.config.JAsyncDictionaryConfig;
import crm.dictionary.web.handler.CountryHandler;
import crm.dictionary.web.resource.CountryOutResource;
import crm.dictionary.web.service.CachedDictionaryService;
import crm.dictionary.web.service.impl.DefaultCachedDictionaryService;
import crm.security.web.WebSecurityConfig;
import crm.base.web.config.ApiEndpoint;
import crm.base.web.config.BaseWebConfig;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Import({
    JAsyncDictionaryConfig.class,
    WebSecurityConfig.class,
    BaseWebConfig.class
})
@PropertySources({
    @PropertySource("classpath:dictionary/web/dictionary-web.properties"),
    @PropertySource(value = "classpath:dictionary/web/dictionary-web-${spring.profiles.active:default}.properties", ignoreResourceNotFound = true)
})
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class DictionaryWebConfig {

  @Bean
  @NotNull CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService(
      @NotNull CountryDao countryDao,
      @NotNull ScheduledExecutorService mainScheduler,
      @Value("${dictionary.web.cache.reload.interval:600}") int reloadInterval) {

    var service = new DefaultCachedDictionaryService<>(countryDao,
        CountryOutResource::from,
        objects -> objects.toArray(CountryOutResource[]::new));
    service.reload();

    mainScheduler.scheduleAtFixedRate(service::reloadAsync, reloadInterval, reloadInterval, TimeUnit.SECONDS);

    return service;
  }

  @Bean
  @NotNull ApiEndpoint dictionaryApiEndpoint() {
    return new ApiEndpoint("/dictionary-api");
  }

  @Bean
  @NotNull CountryHandler countryHandler(
      @NotNull CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService) {
    return new CountryHandler(countryDictionaryService);
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> dictionaryStatusRouterFunction(
      @NotNull ApiEndpoint dictionaryApiEndpoint) {
    var contextPath = dictionaryApiEndpoint.contextPath();
    return RouterFunctions
        .route()
        .GET(
            "%s/status".formatted(contextPath),
            request -> ServerResponse
                .ok()
                .build())
        .build();
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> countryRouterFunction(
      @NotNull ApiEndpoint dictionaryApiEndpoint, @NotNull CountryHandler countryHandler) {
    var contextPath = dictionaryApiEndpoint.contextPath();
    return RouterFunctions
        .route()
        .GET("%s/countries".formatted(contextPath), countryHandler::getAll)
        .GET("%s/country/id/{id}".formatted(contextPath), countryHandler::getById)
        .GET("%s/country/name/{name}".formatted(contextPath), countryHandler::getByName)
        .build();
  }
}
