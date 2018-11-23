package com.ss.jcrm.registration.web;

import com.ss.jcrm.registration.web.config.RegistrationConfig;
import com.ss.jcrm.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Import(RegistrationConfig.class)
//@PropertySource("classpath:com/ss/jcrm/registration/web/application.properties")
public class RegistrationApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {
        System.setProperty("server.servlet.context-path", "/registration");
        SpringApplication.run(RegistrationApplication.class, args);
    }
}
