package com.ss.jcrm.dictionary.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.dictionary.web.resource.AllCountriesOutResource;
import com.ss.jcrm.dictionary.web.resource.CountryOutResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.dictionary.web.service.impl.DefaultCachedDictionaryService;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.web.config.BaseWebConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Import({
    JdbcDictionaryConfig.class,
    WebSecurityConfig.class,
    BaseWebConfig.class
})
@Configuration
@ComponentScan("com.ss.jcrm.dictionary.web")
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/dictionary/web/dictionary-web.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/dictionary/web/dictionary-web-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
public class DictionaryWebConfig {

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private ScheduledExecutorService reloadScheduler;

    @Autowired
    private Environment env;

    @Bean
    @NotNull CachedDictionaryService<CountryOutResource, AllCountriesOutResource> countryDictionaryService() {

        var service = new DefaultCachedDictionaryService<>(
            countryDao,
            CountryOutResource::new,
            AllCountriesOutResource::new
        );
        service.reload();

        int interval = env.getProperty("dictionary.web.cache.reload.interval", Integer.class, 600);

        reloadScheduler.scheduleAtFixedRate(service::reloadAsync, interval, interval, TimeUnit.SECONDS);

        return service;
    }
}
