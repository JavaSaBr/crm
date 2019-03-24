package com.ss.jcrm.registration.web.controller;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.*;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
public class OrganizationController {

    private final UserDao userDao;
    private final UserGroupDao userRoleDao;
    private final OrganizationDao organizationDao;
    private final CountryDao countryDao;
    private final PasswordService passwordService;

    private final int orgNameMinLength;
    private final int orgNameMaxLength;

    @Autowired
    private OrganizationController(
        @NotNull UserDao userDao,
        @NotNull UserGroupDao userRoleDao,
        @NotNull OrganizationDao organizationDao,
        @NotNull CountryDao countryDao,
        @NotNull PasswordService passwordService,
        @Value("${registration.web.organization.name.min.length}") int orgNameMinLength,
        @Value("${registration.web.organization.name.max.length}") int orgNameMaxLength
    ) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.organizationDao = organizationDao;
        this.countryDao = countryDao;
        this.passwordService = passwordService;
        this.orgNameMinLength = orgNameMinLength;
        this.orgNameMaxLength = orgNameMaxLength;
    }

    @PostMapping(
        path = "/registration/organization/register",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<?> register(@NotNull @RequestBody OrganizationRegisterInResource resource) {

        var countryId = resource.getCountryId();
        var orgName = resource.getOrgName();

        if (StringUtils.isEmpty(orgName) ||
            orgName.length() < orgNameMinLength ||
            orgName.length() > orgNameMaxLength
        ) {
            throw new BadRequestWebException(ERROR_ORG_NAME_WRONG_LENGTH);
        }

        var email = resource.getEmail();

        if (!StringUtils.checkEmail(email)) {
            throw new BadRequestWebException(ERROR_INVALID_EMAIL);
        }

        return countryDao.findByIdAsync(countryId)
            .thenCompose(country -> {

                if(country == null) {
                    throw new BadRequestWebException(ERROR_COUNTRY_NOT_FOUND);
                }

                return organizationDao.createAsync(orgName)
                    .exceptionally(throwable -> {

                        if (throwable instanceof DuplicateObjectDaoException) {
                            throw new BadRequestWebException(ERROR_ORG_ALREADY_EXIST);
                        } else {
                            throw new CompletionException(throwable);
                        }
                    })
                    .thenCompose(organization -> {

                        var salt = passwordService.getNextSalt();
                        var hash = passwordService.hash(resource.getPassword(), salt);

                        return userDao.createAsync(email, hash, salt, organization)
                            .exceptionally(throwable -> {

                                organizationDao.delete(organization.getId());

                                if (throwable instanceof DuplicateObjectDaoException) {
                                    throw new BadRequestWebException(ERROR_EMAIL_ALREADY_EXIST);
                                } else {
                                    throw new CompletionException(throwable);
                                }
                            })
                            .thenApply(user -> ResponseEntity.status(HttpStatus.CREATED).build());
                    });
            });
    }

    @GetMapping("/registration/organization/exist/{name}")
    @NotNull CompletableFuture<?> exist(@NotNull @PathVariable("name") String name) {

        if (StringUtils.isEmpty(name) ||
            name.length() < orgNameMinLength ||
            name.length() > orgNameMaxLength
        ) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound());
        }

        return organizationDao.findByNameAsync(name)
            .thenApply(organization -> {
                if (organization == null) {
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.ok().build();
                }
            });
    }
}
