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
import crm.user.jasync.config.JAsyncUserConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.dao.MinimalUserDao;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
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
        return new ApiEndpointServer("/registration-api");
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
            .GET("%s/status".formatted(contextPath), request -> ServerResponse.ok()
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
            .GET("%s/user/minimal/{id}".formatted(contextPath), userHandler::findMinimalById)
            .POST("%s/users/minimal/ids".formatted(contextPath), APP_JSON, userHandler::findMinimalByIds)
            .GET("%s/user/{id}".formatted(contextPath), userHandler::findById)
            .POST("%s/user".formatted(contextPath), APP_JSON, userHandler::create)
            .POST("%s/users/ids".formatted(contextPath), APP_JSON, userHandler::findByIds)
            .GET("%s/users/page".formatted(contextPath), userHandler::findPage)
            .GET("%s/exist/user/email/{email}".formatted(contextPath), userHandler::existByEmail)
            .GET("%s/search/user/name/{name}".formatted(contextPath), userHandler::searchByName)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> emailConfirmationRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull EmailConfirmationHandler emailConfirmationHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("%s/email-confirmation/{email}".formatted(contextPath), emailConfirmationHandler::emailConfirmation)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> authenticationRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull AuthenticationHandler authenticationHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .POST("%s/authenticate".formatted(contextPath), APP_JSON, authenticationHandler::authenticate)
            .GET("%s/authenticate/{token}".formatted(contextPath), authenticationHandler::authenticateByToken)
            .GET("%s/token/refresh/{token}".formatted(contextPath), authenticationHandler::refreshToken)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> organizationRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull OrganizationHandler organizationHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .POST("%s/register/organization".formatted(contextPath), APP_JSON, organizationHandler::register)
            .GET("%s/exist/organization/name/{name}".formatted(contextPath), organizationHandler::existByName)
            .build();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> userGroupRouterFunction(
        @NotNull ApiEndpointServer registrationApiEndpointServer,
        @NotNull UserGroupHandler userGroupHandler
    ) {
        var contextPath = registrationApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .POST("%s/user-group".formatted(contextPath), APP_JSON, userGroupHandler::create)
            .POST("%s/user-groups/ids".formatted(contextPath), APP_JSON, userGroupHandler::findByIds)
            .GET("%s/user-group/{id}".formatted(contextPath), userGroupHandler::findById)
            .PUT("%s/user-group/{id}".formatted(contextPath), userGroupHandler::update)
            .GET("%s/user-groups/page".formatted(contextPath), userGroupHandler::findPage)
            .GET("%s/user-group/{id}/users/page".formatted(contextPath), userGroupHandler::findUsersPage)
            .GET("%s/exist/user-group/name/{name}".formatted(contextPath), userGroupHandler::existByName)
            .build();
    }
}
