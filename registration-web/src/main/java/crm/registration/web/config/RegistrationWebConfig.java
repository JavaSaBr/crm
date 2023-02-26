package crm.registration.web.config;

import crm.dictionary.api.dao.CountryDao;
import crm.dictionary.jasync.config.JAsyncDictionaryConfig;
import crm.mail.config.MailConfig;
import crm.mail.service.MailService;
import crm.registration.web.exception.handler.RegistrationWebExceptionHandler;
import crm.registration.web.handler.*;
import crm.registration.web.validator.ResourceValidator;
import crm.security.service.PasswordService;
import crm.security.web.WebSecurityConfig;
import crm.security.web.service.TokenService;
import crm.security.web.service.WebRequestSecurityService;
import crm.base.spring.template.TemplateRegistry;
import crm.user.jasync.config.JAsyncUserConfig;
import crm.base.web.config.ApiEndpoint;
import crm.base.web.config.BaseWebConfig;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.dao.MinimalUserDao;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
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
@PropertySources({
    @PropertySource("classpath:registration/web/registration-web.properties"),
    @PropertySource(value = "classpath:registration/web/registration-web-${spring.profiles.active:default}.properties", ignoreResourceNotFound = true)
})
@Configuration(proxyBeanMethods = false)
public class RegistrationWebConfig {

  private static final RequestPredicate APP_JSON = RequestPredicates.contentType(MediaType.APPLICATION_JSON);

  @Bean
  @NotNull ResourceValidator registrationResourceValidator(@NotNull Environment env) {
    return new ResourceValidator(env);
  }

  @Bean
  @NotNull TemplateRegistry emailCodeTemplate() {
    return new TemplateRegistry("registration/web/templates/reg_activate.template");
  }

  @Bean
  @NotNull ApiEndpoint registrationApiEndpoint() {
    return new ApiEndpoint("/registration-api");
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
      @NotNull PasswordService passwordService) {
    return new UserHandler(userDao, resourceValidator, webRequestSecurityService, passwordService);
  }

  @Bean
  @NotNull EmailConfirmationHandler emailConfirmationHandler(
      @NotNull EmailConfirmationDao emailConfirmationDao,
      @NotNull TokenService tokenService,
      @NotNull ResourceValidator resourceValidator,
      @NotNull TemplateRegistry emailConfirmationTemplate,
      @NotNull MailService mailService,
      @Value("${registration.web.email.confirmation.email.subject}") @NotNull String subject,
      @Value("${registration.web.email.confirmation.activate.code.length}") int codeLength,
      @Value("${registration.web.email.confirmation.expiration}") int expiration) {
    return new EmailConfirmationHandler(
        emailConfirmationDao,
        tokenService,
        resourceValidator,
        emailConfirmationTemplate,
        mailService,
        subject,
        codeLength,
        expiration);
  }

  @Bean
  @NotNull AuthenticationHandler authenticationHandler(
      @NotNull TokenService tokenService,
      @NotNull UserDao userDao,
      @NotNull PasswordService passwordService,
      @NotNull ResourceValidator resourceValidator) {
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
      @NotNull TokenService tokenService) {
    return new OrganizationHandler(
        userDao,
        organizationDao,
        countryDao,
        passwordService,
        resourceValidator,
        emailConfirmationDao,
        tokenService);
  }

  @Bean
  @NotNull UserGroupHandler userGroupHandler(
      @NotNull UserDao userDao,
      @NotNull UserGroupDao userGroupDao,
      @NotNull MinimalUserDao minimalUserDao,
      @NotNull ResourceValidator resourceValidator,
      @NotNull WebRequestSecurityService webRequestSecurityService) {
    return new UserGroupHandler(userDao, userGroupDao, minimalUserDao, resourceValidator, webRequestSecurityService);
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> registrationStatusRouterFunction(
      @NotNull ApiEndpoint registrationApiEndpoint) {
    var contextPath = registrationApiEndpoint.contextPath();
    return RouterFunctions
        .route()
        .GET(
            "%s/status".formatted(contextPath),
            request -> ServerResponse
                .ok()
                .build())
        .build();
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> userRouterFunction(
      @NotNull ApiEndpoint registrationApiEndpoint, @NotNull UserHandler userHandler) {
    var contextPath = registrationApiEndpoint.contextPath();
    return RouterFunctions
        .route()
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
      @NotNull ApiEndpoint registrationApiEndpoint, @NotNull EmailConfirmationHandler emailConfirmationHandler) {
    var contextPath = registrationApiEndpoint.contextPath();
    return RouterFunctions
        .route()
        .GET("%s/email-confirmation/{email}".formatted(contextPath), emailConfirmationHandler::emailConfirmation)
        .build();
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> authenticationRouterFunction(
      @NotNull ApiEndpoint registrationApiEndpoint, @NotNull AuthenticationHandler authenticationHandler) {
    var contextPath = registrationApiEndpoint.contextPath();
    return RouterFunctions
        .route()
        .POST("%s/authenticate".formatted(contextPath), APP_JSON, authenticationHandler::authenticate)
        .GET("%s/authenticate/{token}".formatted(contextPath), authenticationHandler::authenticateByToken)
        .GET("%s/token/refresh/{token}".formatted(contextPath), authenticationHandler::refreshToken)
        .build();
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> organizationRouterFunction(
      @NotNull ApiEndpoint registrationApiEndpoint, @NotNull OrganizationHandler organizationHandler) {
    var contextPath = registrationApiEndpoint.contextPath();
    return RouterFunctions
        .route()
        .POST("%s/register/organization".formatted(contextPath), APP_JSON, organizationHandler::register)
        .GET("%s/exist/organization/name/{name}".formatted(contextPath), organizationHandler::existByName)
        .build();
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> userGroupRouterFunction(
      @NotNull ApiEndpoint registrationApiEndpoint, @NotNull UserGroupHandler userGroupHandler) {
    var contextPath = registrationApiEndpoint.contextPath();
    return RouterFunctions
        .route()
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
