package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.registration.web.resources.UserRegisterResource;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.rlib.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
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
        @NotNull @RequestBody UserRegisterResource resource
    ) {

        String name = resource.getName();
        String password = resource.getPassword();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {

        }

        long[] roles = resource.getRoles();

        return CompletableFuture.completedFuture(ResponseEntity.ok());
    }
}
