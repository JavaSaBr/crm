package com.ss.jcrm.dictionary.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.dictionary.web.resource.AllCountriesResource;
import com.ss.jcrm.dictionary.web.resource.CountryResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.dictionary.web.service.impl.DefaultCachedDictionaryService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAutoConfiguration
@Import({JdbcDictionaryConfig.class})
@AllArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("com.ss.jcrm.dictionary.web")
public class DictionaryConfig {

    private final CountryDao countryDao;
    private final ScheduledExecutorService reloadScheduler;

    @Bean
    @NotNull CachedDictionaryService<CountryResource, AllCountriesResource> countryDictionaryService() {

        var service = new DefaultCachedDictionaryService<>(
            countryDao,
            CountryResource::new,
            AllCountriesResource::new
        );
        service.reload();

        reloadScheduler.scheduleAtFixedRate(service::reloadAsync, 600, 600, TimeUnit.SECONDS);

        return service;
    }
}
