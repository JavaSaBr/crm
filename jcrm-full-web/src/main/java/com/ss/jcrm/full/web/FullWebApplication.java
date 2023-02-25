package com.ss.jcrm.full.web;

import com.ss.jcrm.dictionary.web.config.DictionaryWebConfig;
import com.ss.jcrm.registration.web.config.RegistrationWebConfig;
import com.ss.jcrm.ui.server.config.UiServerConfig;
import crm.base.web.BaseWebApplication;
import crm.client.web.config.ClientWebConfig;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
    RegistrationWebConfig.class,
    DictionaryWebConfig.class,
    ClientWebConfig.class,
    UiServerConfig.class
})
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class FullWebApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {

        if (System.getProperty("server.port") == null) {
            System.setProperty("server.port", "8090");
        }

        SpringApplication.run(FullWebApplication.class, args);
    }

    private final @NotNull List<? extends Flyway> flyways;
}
