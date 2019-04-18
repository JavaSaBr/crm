package com.ss.jcrm.registration.web.controller;

import com.ss.jcrm.registration.web.resources.AuthenticationInResource;
import com.ss.jcrm.registration.web.resources.AuthenticationOutResource;
import com.ss.jcrm.registration.web.resources.UserOutResource;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.controller.AsyncRestController;
import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
public class AuthenticationController extends AsyncRestController {

    private final TokenService tokenService;
    private final UserDao userDao;
    private final PasswordService passwordService;

    @Autowired
    public AuthenticationController(
        @NotNull Executor controllerExecutor,
        @NotNull TokenService tokenService,
        @NotNull UserDao userDao,
        @NotNull PasswordService passwordService
    ) {
        super(controllerExecutor);
        this.tokenService = tokenService;
        this.userDao = userDao;
        this.passwordService = passwordService;
    }

    @PostMapping(
        path = "/registration/authenticate",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<@NotNull AuthenticationOutResource> authenticate(
        @NotNull @RequestBody AuthenticationInResource resource
    ) {

        var name = resource.getName();
        var password = resource.getPassword();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            throw new BadRequestWebException("The name or password is empty.");
        }

        return userDao.findByNameAsync(name)
            .thenApplyAsync(user -> {

                if (user == null) {
                    throw new BadRequestWebException("The name or password isn't correct.");
                } else if (!passwordService.isCorrect(password, user.getSalt(), user.getPassword())) {
                    throw new BadRequestWebException("The name or password isn't correct.");
                }

                return new AuthenticationOutResource(
                    new UserOutResource(user),
                    tokenService.generateNewToken(user)
                );

            }, controllerExecutor);
    }
}
