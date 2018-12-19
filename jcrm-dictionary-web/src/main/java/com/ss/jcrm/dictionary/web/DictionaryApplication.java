package com.ss.jcrm.dictionary.web;

import com.ss.jcrm.dictionary.web.config.DictionaryConfig;
import com.ss.jcrm.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DictionaryConfig.class)
public class DictionaryApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {
        System.setProperty("server.servlet.context-path", "/dictionary");
        SpringApplication.run(DictionaryApplication.class, args);
    }
}
