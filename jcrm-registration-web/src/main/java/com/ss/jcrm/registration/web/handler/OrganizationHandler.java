package com.ss.jcrm.registration.web.handler;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.*;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.registration.web.resources.AuthenticationOutResource;
import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.user.api.EmailConfirmation;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.jcrm.web.exception.ExceptionUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RequiredArgsConstructor
public class OrganizationHandler {

    private final UserDao userDao;
    private final OrganizationDao organizationDao;
    private final CountryDao countryDao;
    private final PasswordService passwordService;
    private final ResourceValidator resourceValidator;
    private final EmailConfirmationDao emailConfirmationDao;
    private final TokenService tokenService;

    public @NotNull Mono<ServerResponse> register(@NotNull ServerRequest request) {
        return request.bodyToMono(OrganizationRegisterInResource.class)
            .doOnNext(resourceValidator::validate)
            .zipWhen(this::findEmailConfirmation, this::validateConfirmation)
            .switchIfEmpty(Mono.error(() -> new BadRequestWebException(INVALID_ACTIVATION_CODE_MESSAGE, INVALID_ACTIVATION_CODE)))
            .zipWhen(this::findCountry, this::createOrganization)
            .flatMap(Function.identity())
            .switchIfEmpty(Mono.error(() -> new BadRequestWebException(COUNTRY_NOT_FOUND_MESSAGE, COUNTRY_NOT_FOUND)))
            .flatMap(resource -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(resource));
    }

    public @NotNull Mono<ServerResponse> existByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .map(organizationDao::existByNameAsync)
            .flatMap(Mono::fromFuture)
            .switchIfEmpty(Mono.just(false))
            .map(exist -> exist ? HttpStatus.OK : HttpStatus.NOT_FOUND)
            .flatMap(status -> ServerResponse.status(status)
                .build());

    }

    private @NotNull Mono<EmailConfirmation> findEmailConfirmation(@NotNull OrganizationRegisterInResource resource) {
        return Mono.fromFuture(emailConfirmationDao.findByEmailAndCodeAsync(
            resource.getEmail(),
            resource.getActivationCode()
        ));
    }

    private <T> @Nullable T validateConfirmation(@NotNull T resource, @NotNull EmailConfirmation confirmation) {
        if (Instant.now().isAfter(confirmation.getExpiration())) {
            return null;
        } else {
            return resource;
        }
    }

    private @NotNull Mono<@Nullable Country> findCountry(@NotNull OrganizationRegisterInResource resource) {
        return Mono.fromFuture(countryDao.findByIdAsync(resource.getCountryId()));
    }

    private @NotNull Mono<AuthenticationOutResource> createOrganization(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Country country
    ) {
        return Mono.fromFuture(organizationDao.createAsync(resource.getOrgName(), country)
            .exceptionally(throwable -> ExceptionUtils.badRequest(throwable,
                DuplicateObjectDaoException.class::isInstance,
                ORG_ALREADY_EXIST,
                ORG_ALREADY_EXIST_MESSAGE
            ))
            .thenCompose(organization -> createOrgAdmin(resource, organization)));
    }

    private @NotNull CompletableFuture<AuthenticationOutResource> createOrgAdmin(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Organization organization
    ) {

        var salt = passwordService.getNextSalt();
        var hash = passwordService.hash(resource.getPassword(), salt);

        Arrays.fill(resource.getPassword(), ' ');

        return createOrgAdmin(resource, organization, salt, hash)
            .handle((user, throwable) -> {

                if (user != null) {
                    return CompletableFuture.completedFuture(user);
                } else {
                    return organizationDao.deleteAsync(organization.getId())
                        .thenApply(wasDeleted -> ExceptionUtils.<User>badRequest(
                            throwable,
                            DuplicateObjectDaoException.class::isInstance,
                            EMAIL_ALREADY_EXIST,
                            EMAIL_ALREADY_EXIST_MESSAGE
                        ));
                }

            })
            .thenCompose(Function.identity())
            .thenApply(user -> new AuthenticationOutResource(user, tokenService.generateNewToken(user)));
    }

    private @NotNull CompletableFuture<@NotNull User> createOrgAdmin(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Organization organization,
        @NotNull byte[] salt,
        @NotNull byte[] hash
    ) {
        return userDao.createAsync(
            resource.getEmail(),
            hash,
            salt,
            organization,
            Set.of(AccessRole.ORG_ADMIN),
            resource.getFirstName(),
            resource.getSecondName(),
            resource.getThirdName(),
            resource.getPhoneNumber()
        );
    }
}
