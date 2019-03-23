package com.ss.jcrm.registration.web;

import com.ss.jcrm.registration.web.config.RegistrationWebConfig;
import com.ss.jcrm.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegistrationWebConfig.class)
public class RegistrationApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {
        SpringApplication.run(RegistrationApplication.class, args);
    }
}
