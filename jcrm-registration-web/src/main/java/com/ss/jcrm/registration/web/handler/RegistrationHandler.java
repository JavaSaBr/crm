package com.ss.jcrm.registration.web.handler;

import crm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.registration.web.resources.UserRegisterInResource;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import com.ss.rlib.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationHandler {

    private final UserDao userDao;
    private final UserGroupDao userRoleDao;
    private final OrganizationDao organizationDao;
    private final CountryDao countryDao;

    @PostMapping(
        path = "/registration/register",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<?> register(
        @RequestHeader("token") @NotNull String token,
        @NotNull @RequestBody UserRegisterInResource resource
    ) {

        String name = resource.name();
        String password = resource.password();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {

        }

        long[] roles = resource.roles();

        return CompletableFuture.completedFuture(ResponseEntity.ok());
    }
}
