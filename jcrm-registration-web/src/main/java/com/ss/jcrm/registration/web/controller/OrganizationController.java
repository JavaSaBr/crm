package com.ss.jcrm.registration.web.controller;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.*;
import static com.ss.jcrm.web.exception.ExceptionUtils.webException;
import static org.springframework.http.ResponseEntity.*;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.mail.service.MailService;
import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.exception.BadRequestWebException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class OrganizationController {

    private final UserDao userDao;
    private final OrganizationDao organizationDao;
    private final CountryDao countryDao;
    private final PasswordService passwordService;
    private final ResourceValidator resourceValidator;
    private final TemplateRegistry emailCodeTemplate;
    private final MailService mailService;

    @PostMapping(
        path = "/registration/organization/register",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<ResponseEntity<?>> register(
        @NotNull @RequestBody OrganizationRegisterInResource resource
    ) {
        resourceValidator.validate(resource);
        return countryDao.findByIdAsync(resource.getCountryId())
            .thenCompose(country -> {
                if(country == null) {
                    throw new BadRequestWebException(COUNTRY_NOT_FOUND);
                } else {
                    return createOrganization(resource, country);
                }
            });
    }

    private CompletionStage<ResponseEntity<?>> createOrganization(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Country country
    ) {
        return organizationDao.createAsync(resource.getOrgName(), country)
            .exceptionally(throwable -> webException(
                throwable,
                DuplicateObjectDaoException.class::isInstance, ORG_ALREADY_EXIST
                           )
            )
            .thenCompose(organization -> createOrgAdmin(resource, organization));
    }

    private @NotNull CompletionStage<ResponseEntity<?>> createOrgAdmin(
        @NotNull OrganizationRegisterInResource resource,
        @NotNull Organization organization
    ) {

        var salt = passwordService.getNextSalt();
        var hash = passwordService.hash(resource.getPassword(), salt);

        resource.setPassword(null);

        return createOrgAdmin(resource, organization, salt, hash)
            .exceptionally(throwable -> {

                organizationDao.delete(organization.getId());

                return webException(
                    throwable,
                    DuplicateObjectDaoException.class::isInstance, EMAIL_ALREADY_EXIST
                );

            })
            .thenApply(user -> status(HttpStatus.CREATED).build());
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

    @GetMapping("/registration/organization/exist/name/{name}")
    @NotNull CompletableFuture<ResponseEntity<?>> exist(@NotNull @PathVariable("name") String name) {
        resourceValidator.validateOrgName(name);
        return organizationDao.existByNameAsync(name)
            .thenApply(exist -> new ResponseEntity<>(exist ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
