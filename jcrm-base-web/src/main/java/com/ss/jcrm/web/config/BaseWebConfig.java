package com.ss.jcrm.web.config;

import com.ss.jcrm.web.converter.JsoniterHttpMessageConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseWebConfig {

    @Bean
    @NotNull HttpMessageConverters customConverters() {
        return new HttpMessageConverters(new JsoniterHttpMessageConverter());
    }
}
