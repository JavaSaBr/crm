package com.ss.jcrm.dictionary.web;

import com.ss.jcrm.dictionary.web.config.DictionaryWebConfig;
import com.ss.jcrm.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DictionaryWebConfig.class)
public class DictionaryApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {
        SpringApplication.run(DictionaryApplication.class, args);
    }
}
