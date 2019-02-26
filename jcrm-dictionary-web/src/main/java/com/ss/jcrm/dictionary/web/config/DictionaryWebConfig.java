package com.ss.jcrm.dictionary.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.dictionary.web.resource.AllCountriesResource;
import com.ss.jcrm.dictionary.web.resource.CountryResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.dictionary.web.service.impl.DefaultCachedDictionaryService;
import com.ss.jcrm.web.config.BaseWebConfig;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@Import({
    JdbcDictionaryConfig.class,
    BaseWebConfig.class
})
@ComponentScan("com.ss.jcrm.dictionary.web")
@AllArgsConstructor(onConstructor_ = @Autowired)
@PropertySource("classpath:com.ss.jcrm.dictionary.web/dictionary-web.properties")
public class DictionaryWebConfig {

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
