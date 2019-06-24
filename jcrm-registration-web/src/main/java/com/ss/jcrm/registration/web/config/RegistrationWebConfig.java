package com.ss.jcrm.registration.web.config;

import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.mail.MailConfig;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import com.ss.jcrm.web.exception.handler.BaseWebExceptionHandler;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.List;

@Import({
    BaseWebConfig.class,
    JdbcUserConfig.class,
    JdbcDictionaryConfig.class,
    WebSecurityConfig.class,
    MailConfig.class
})
@Configuration
@ComponentScan("com.ss.jcrm.registration.web")
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/registration/web/registration-web.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/registration/web/registration-web-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
public class RegistrationWebConfig {

    @Autowired
    private Environment env;

    @Autowired
    private List<? extends Flyway> flyways;

    @Bean
    @NotNull ResourceValidator resourceValidator() {
        return new ResourceValidator(env);
    }

    @Bean
    @NotNull TemplateRegistry emailCodeTemplate() {
        return new TemplateRegistry("com/ss/jcrm/registration/web/templates/reg_activate.template");
    }

    @Bean
    @NotNull BaseWebExceptionHandler webExceptionHandler() {
        return new BaseWebExceptionHandler();
    }

    @Bean
    @NotNull ApiEndpointServer registrationApiEndpointServer() {
        return new ApiEndpointServer("/registration");
    }
}
