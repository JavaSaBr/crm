package com.ss.jcrm.web.config;

import com.ss.jcrm.web.converter.JsoniterHttpMessageConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class BaseWebConfig extends WebMvcConfigurationSupport {

    @Override
    protected void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        converters.add(new JsoniterHttpMessageConverter());
        super.configureMessageConverters(converters);
    }
}
