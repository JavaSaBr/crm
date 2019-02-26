package com.ss.jcrm.registration.web.controller;

import com.ss.jcrm.registration.web.resources.UserRegisterResource;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserRoleDao;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class RegistrationController {

    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final OrganizationDao organizationDao;

    @Autowired
    public RegistrationController(
        @NotNull UserDao userDao,
        @NotNull UserRoleDao userRoleDao,
        @NotNull OrganizationDao organizationDao
    ) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.organizationDao = organizationDao;
    }

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
