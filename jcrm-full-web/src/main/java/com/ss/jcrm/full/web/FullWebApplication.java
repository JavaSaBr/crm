package com.ss.jcrm.full.web;

import com.ss.jcrm.dictionary.web.config.DictionaryWebConfig;
import com.ss.jcrm.registration.web.config.RegistrationWebConfig;
import com.ss.jcrm.ui.server.config.UiServerConfig;
import com.ss.jcrm.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

@Import({
    RegistrationWebConfig.class,
    DictionaryWebConfig.class,
    UiServerConfig.class
})
@Configuration
public class FullWebApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {

        if (System.getProperty("server.port") == null) {
            System.setProperty("server.port", "8090");
        }

        SpringApplication.run(FullWebApplication.class, args);
    }
}
