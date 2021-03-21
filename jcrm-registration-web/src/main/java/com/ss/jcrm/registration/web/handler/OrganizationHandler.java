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
import com.ss.jcrm.web.util.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizationHandler extends BaseRegistrationHandler {

    @NotNull UserDao userDao;
    @NotNull OrganizationDao organizationDao;
    @NotNull CountryDao countryDao;
    @NotNull PasswordService passwordService;
    @NotNull ResourceValidator resourceValidator;
    @NotNull EmailConfirmationDao emailConfirmationDao;
    @NotNull TokenService tokenService;

    public @NotNull Mono<ServerResponse> register(@NotNull ServerRequest request) {
        return request.bodyToMono(OrganizationRegisterInResource.class)
            .doOnNext(resourceValidator::validate)
            .zipWhen(this::findEmailConfirmation, this::validateConfirmation)
            .switchIfEmpty(Mono.error(() -> new BadRequestWebException(
                INVALID_ACTIVATION_CODE_MESSAGE,
                INVALID_ACTIVATION_CODE))
            )
            .zipWhen(this::findCountry, this::createOrganization)
            .flatMap(Function.identity())
            .switchIfEmpty(Mono.error(() -> new BadRequestWebException(
                COUNTRY_NOT_FOUND_MESSAGE,
                COUNTRY_NOT_FOUND))
            )
            .flatMap(ResponseUtils::created);
    }

    public @NotNull Mono<ServerResponse> existByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .flatMap(organizationDao::existByName)
            .switchIfEmpty(Mono.just(false))
            .flatMap(ResponseUtils::exist);
    }

    private @NotNull Mono<EmailConfirmation> findEmailConfirmation(@NotNull OrganizationRegisterInResource resource) {
        return emailConfirmationDao.findByEmailAndCode(
            resource.email(),
            resource.activationCode()
        );
    }

    private <T> @Nullable T validateConfirmation(@NotNull T resource, @NotNull EmailConfirmation confirmation) {
        if (Instant.now().isAfter(confirmation.getExpiration())) {
            return null;
        } else {
            return resource;
        }
    }

    private @NotNull Mono<@Nullable Country> findCountry(@NotNull OrganizationRegisterInResource resource) {
        return countryDao.findById(resource.countryId());
    }

    private @NotNull Mono<AuthenticationOutResource> createOrganization(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Country country
    ) {
        return organizationDao.create(resource.orgName(), country)
            .onErrorResume(throwable -> Mono.error(ExceptionUtils.toBadRequest(throwable,
                DuplicateObjectDaoException.class::isInstance,
                ORG_ALREADY_EXIST,
                ORG_ALREADY_EXIST_MESSAGE
            )))
            .flatMap(organization -> createOrgAdmin(resource, organization));
    }

    private @NotNull Mono<AuthenticationOutResource> createOrgAdmin(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Organization organization
    ) {

        var salt = passwordService.getNextSalt();
        var hash = passwordService.hash(resource.password(), salt);

        Arrays.fill(resource.password(), ' ');

        return createOrgAdmin(resource, organization, salt, hash)
            .onErrorResume(throwable -> organizationDao.delete(organization.getId())
                .flatMap(aBoolean -> Mono.error(ExceptionUtils.toBadRequest(
                    throwable,
                    DuplicateObjectDaoException.class::isInstance,
                    EMAIL_ALREADY_EXIST,
                    EMAIL_ALREADY_EXIST_MESSAGE
                ))))
            .map(user -> AuthenticationOutResource.from(tokenService.generateNewToken(user), user));
    }

    private @NotNull Mono<@NotNull User> createOrgAdmin(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Organization organization,
        byte @NotNull [] salt,
        byte @NotNull [] hash
    ) {
        return userDao.create(
            resource.email(),
            hash,
            salt,
            organization,
            Set.of(AccessRole.ORG_ADMIN),
            resource.firstName(),
            resource.secondName(),
            resource.thirdName(),
            null,
            Set.of(resource.phoneNumber().toPhoneNumber()),
            Set.of()
        );
    }

}
