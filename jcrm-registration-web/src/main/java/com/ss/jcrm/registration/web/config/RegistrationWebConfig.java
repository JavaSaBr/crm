package com.ss.jcrm.registration.web.config;

import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.mail.MailConfig;
import com.ss.jcrm.registration.web.exception.handler.RegistrationWebExceptionHandler;
import com.ss.jcrm.registration.web.handler.UserHandler;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;

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
    @NotNull ApiEndpointServer registrationApiEndpointServer() {
        return new ApiEndpointServer("/registration");
    }

    @Bean
    @NotNull WebExceptionHandler registrationWebExceptionHandler() {
        return new RegistrationWebExceptionHandler();
    }

    @Bean
    @NotNull UserHandler userHandler(@NotNull UserDao userDao, @NotNull ResourceValidator resourceValidator) {
        return new UserHandler(userDao, resourceValidator);
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> registrationStatusRouterFunction() {
        return RouterFunctions.route()
            .GET("/registration/status", request -> ServerResponse.ok()
                .build())
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> userRouterFunction(@NotNull UserHandler userHandler) {
        return RouterFunctions.route()
            .GET("/registration/exist/user/email/{email}", userHandler::existByEmail)
            .build();
    }
}
