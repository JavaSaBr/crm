package com.ss.jcrm.registration.web.controller;

import com.ss.jcrm.registration.web.resources.AuthenticationResource;
import com.ss.jcrm.registration.web.resources.UserRegisterResource;
import com.ss.jcrm.security.web.token.TokenService;
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
public class AuthenticationController {

    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(
        @NotNull TokenService tokenService
    ) {
        this.tokenService = tokenService;
    }

    @PostMapping(
        path = "/auth",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<?> auth(@NotNull @RequestBody AuthenticationResource resource) {

        String name = resource.getName();
        String password = resource.getPassword();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {

        }

        long[] roles = resource.getRoles();

        return CompletableFuture.completedFuture(ResponseEntity.ok());
    }
}
