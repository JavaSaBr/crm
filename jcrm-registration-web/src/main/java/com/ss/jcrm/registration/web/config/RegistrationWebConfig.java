package com.ss.jcrm.registration.web.config;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.jasync.config.JAsyncDictionaryConfig;
import com.ss.jcrm.mail.MailConfig;
import com.ss.jcrm.mail.service.MailService;
import com.ss.jcrm.registration.web.exception.handler.RegistrationWebExceptionHandler;
import com.ss.jcrm.registration.web.handler.*;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.api.dao.*;
import com.ss.jcrm.user.jasync.config.JAsyncUserConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
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
@RequiredArgsConstructor
public class RegistrationWebConfig {

    private static final RequestPredicate APP_JSON = RequestPredicates.contentType(MediaType.APPLICATION_JSON);

    private final Environment env;
    private final List<? extends Flyway> flyways;

    @Bean
    @NotNull ResourceValidator registrationResourceValidator() {
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
    @NotNull UserHandler userHandler(
        @NotNull UserDao userDao,
        @NotNull ResourceValidator resourceValidator,
        @NotNull WebRequestSecurityService webRequestSecurityService,
        @NotNull PasswordService passwordService
    ) {
        return new UserHandler(userDao, resourceValidator, webRequestSecurityService, passwordService);
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
    @NotNull UserGroupHandler userGroupHandler(
        @NotNull UserDao userDao,
        @NotNull UserGroupDao userGroupDao,
        @NotNull MinimalUserDao minimalUserDao,
        @NotNull ResourceValidator resourceValidator,
        @NotNull WebRequestSecurityService webRequestSecurityService
    ) {

        return new UserGroupHandler(
            userDao,
            userGroupDao,
            minimalUserDao,
            resourceValidator,
            webRequestSecurityService
        );
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> registrationStatusRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("${contextPath}/status", request -> ServerResponse.ok()
                .build())
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> userRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull UserHandler userHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("${contextPath}/user/minimal/{id}", userHandler::findMinimalById)
            .POST("${contextPath}/users/minimal/ids", APP_JSON, userHandler::findMinimalByIds)
            .GET("${contextPath}/user/{id}", userHandler::findById)
            .POST("${contextPath}/user", APP_JSON, userHandler::create)
            .POST("${contextPath}/users/ids", APP_JSON, userHandler::findByIds)
            .GET("${contextPath}/users/page", userHandler::findPage)
            .GET("${contextPath}/exist/user/email/{email}", userHandler::existByEmail)
            .GET("${contextPath}/search/user/name/{name}", userHandler::searchByName)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> emailConfirmationRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull EmailConfirmationHandler emailConfirmationHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("${contextPath}/email-confirmation/{email}", emailConfirmationHandler::emailConfirmation)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> authenticationRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull AuthenticationHandler authenticationHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .POST("${contextPath}/authenticate", APP_JSON, authenticationHandler::authenticate)
            .GET("${contextPath}/authenticate/{token}", authenticationHandler::authenticateByToken)
            .GET("${contextPath}/token/refresh/{token}", authenticationHandler::refreshToken)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> organizationRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull OrganizationHandler organizationHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .POST("${contextPath}/register/organization", APP_JSON, organizationHandler::register)
            .GET("${contextPath}/exist/organization/name/{name}", organizationHandler::existByName)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> userGroupRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull UserGroupHandler userGroupHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .POST("${contextPath}/user-group", APP_JSON, userGroupHandler::create)
            .POST("${contextPath}/user-groups/ids", APP_JSON, userGroupHandler::findByIds)
            .GET("${contextPath}/user-group/{id}", userGroupHandler::findById)
            .PUT("${contextPath}/user-group/{id}", userGroupHandler::update)
            .GET("${contextPath}/user-groups/page", userGroupHandler::findPage)
            .GET("${contextPath}/user-group/{id}/users/page", userGroupHandler::findUsersPage)
            .GET("${contextPath}/exist/user-group/name/{name}", userGroupHandler::existByName)
            .build();
    }
}
