package com.ss.jcrm.registration.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jasync.config.JAsyncDictionaryConfig;
import com.ss.jcrm.mail.MailConfig;
import com.ss.jcrm.mail.service.MailService;
import com.ss.jcrm.registration.web.exception.handler.RegistrationWebExceptionHandler;
import com.ss.jcrm.registration.web.handler.AuthenticationHandler;
import com.ss.jcrm.registration.web.handler.EmailConfirmationHandler;
import com.ss.jcrm.registration.web.handler.OrganizationHandler;
import com.ss.jcrm.registration.web.handler.UserHandler;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.jasync.config.JAsyncUserConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.WebExceptionHandler;

import java.util.List;

@Import({
    BaseWebConfig.class,
    JAsyncUserConfig.class,
    JAsyncDictionaryConfig.class,
    WebSecurityConfig.class,
    MailConfig.class
})
@Configuration
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/registration/web/registration-web.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/registration/web/registration-web-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RegistrationWebConfig {

    private static final RequestPredicate APP_JSON =
        RequestPredicates.contentType(MediaType.APPLICATION_JSON_UTF8);

    @Autowired
    private final Environment env;

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
    @NotNull EmailConfirmationHandler emailConfirmationHandler(
        @NotNull EmailConfirmationDao emailConfirmationDao,
        @NotNull TokenService tokenService,
        @NotNull ResourceValidator resourceValidator,
        @NotNull TemplateRegistry emailConfirmationTemplate,
        @NotNull MailService mailService
    ) {

        return new EmailConfirmationHandler(
            emailConfirmationDao,
            tokenService,
            resourceValidator,
            emailConfirmationTemplate,
            mailService,
            env.getRequiredProperty("registration.web.email.confirmation.email.subject"),
            env.getRequiredProperty("registration.web.email.confirmation.activate.code.length", int.class),
            env.getRequiredProperty("registration.web.email.confirmation.expiration", int.class)
        );
    }

    @Bean
    @NotNull AuthenticationHandler authenticationHandler(
        @NotNull TokenService tokenService,
        @NotNull UserDao userDao,
        @NotNull PasswordService passwordService,
        @NotNull ResourceValidator resourceValidator
    ) {
        return new AuthenticationHandler(tokenService, userDao, passwordService, resourceValidator);
    }

    @Bean
    @NotNull OrganizationHandler organizationHandler(
        @NotNull UserDao userDao,
        @NotNull OrganizationDao organizationDao,
        @NotNull CountryDao countryDao,
        @NotNull PasswordService passwordService,
        @NotNull ResourceValidator resourceValidator,
        @NotNull EmailConfirmationDao emailConfirmationDao,
        @NotNull TokenService tokenService
    ) {
        return new OrganizationHandler(
            userDao,
            organizationDao,
            countryDao,
            passwordService,
            resourceValidator,
            emailConfirmationDao,
            tokenService
        );
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

    @Bean
    @NotNull RouterFunction<ServerResponse> emailConfirmationRouterFunction(
        @NotNull EmailConfirmationHandler emailConfirmationHandler
    ) {
        return RouterFunctions.route()
            .GET("/registration/email/confirmation/{email}", emailConfirmationHandler::emailConfirmation)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> authenticationRouterFunction(
        @NotNull AuthenticationHandler authenticationHandler
    ) {
        return RouterFunctions.route()
            .POST("/registration/authenticate", APP_JSON, authenticationHandler::authenticate)
            .GET("/registration/authenticate/{token}", authenticationHandler::authenticateByToken)
            .GET("/registration/token/refresh/{token}", authenticationHandler::refreshToken)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> organizationRouterFunction(
        @NotNull OrganizationHandler organizationHandler
    ) {
        return RouterFunctions.route()
            .POST("/registration/register/organization", APP_JSON, organizationHandler::register)
            .GET("/registration/exist/organization/name/{name}", organizationHandler::existByName)
            .build();
    }
}
