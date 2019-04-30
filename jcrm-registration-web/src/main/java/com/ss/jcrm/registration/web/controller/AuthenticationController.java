package com.ss.jcrm.registration.web.controller;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS;
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS_MESSAGE;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.registration.web.resources.AuthenticationInResource;
import com.ss.jcrm.registration.web.resources.AuthenticationOutResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.controller.AsyncRestController;
import com.ss.jcrm.web.exception.ExceptionUtils;
import com.ss.jcrm.web.exception.UnauthorizedWebException;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
public class AuthenticationController extends AsyncRestController {

    private final TokenService tokenService;
    private final UserDao userDao;
    private final PasswordService passwordService;
    private final ResourceValidator resourceValidator;

    @Autowired
    public AuthenticationController(
        @NotNull Executor controllerExecutor,
        @NotNull TokenService tokenService,
        @NotNull UserDao userDao,
        @NotNull PasswordService passwordService,
        @NotNull ResourceValidator resourceValidator
    ) {
        super(controllerExecutor);
        this.tokenService = tokenService;
        this.userDao = userDao;
        this.passwordService = passwordService;
        this.resourceValidator = resourceValidator;
    }

    @PostMapping(
        path = "/registration/authenticate",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<@NotNull AuthenticationOutResource> authenticate(
        @NotNull @RequestBody AuthenticationInResource resource
    ) {

        resourceValidator.validate(resource);

        var login = resource.getLogin();

        var asyncUser = StringUtils.isEmail(login) ?
                userDao.findByEmailAsync(login) : userDao.findByPhoneNumberAsync(login);

        return asyncUser.thenApplyAsync(user -> {

            var password = resource.getPassword();
            try {

                if (user == null) {
                    throw new UnauthorizedWebException(INVALID_CREDENTIALS_MESSAGE, INVALID_CREDENTIALS);
                } else if (!passwordService.isCorrect(password, user.getSalt(), user.getPassword())) {
                    throw new UnauthorizedWebException(INVALID_CREDENTIALS_MESSAGE, INVALID_CREDENTIALS);
                }

            } finally {
                Arrays.fill(password, ' ');
            }

            return new AuthenticationOutResource(user, tokenService.generateNewToken(user));

        }, controllerExecutor);
    }

    @GetMapping(
        path = "/registration/authenticate/{token}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<@NotNull AuthenticationOutResource> authenticate(
        @NotNull @PathVariable("token") String token
    ) {
        return tokenService.findUserIfNotExpiredAsync(token)
            .exceptionally(throwable -> ExceptionUtils.unauthorized(
                throwable,
                ObjectNotFoundDaoException.class::isInstance,
                RegistrationErrors.INVALID_TOKEN,
                RegistrationErrors.INVALID_TOKEN_MESSAGE
            ))
            .thenApply(user -> new AuthenticationOutResource(user, token));
    }

    @GetMapping(
        path = "/registration/token/refresh/{token}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<@NotNull AuthenticationOutResource> refresh(
        @NotNull @PathVariable("token") String token
    ) {
        return tokenService.findUserAsync(token)
            .exceptionally(throwable -> ExceptionUtils.badRequest(
                throwable,
                ObjectNotFoundDaoException.class::isInstance,
                RegistrationErrors.INVALID_TOKEN,
                RegistrationErrors.INVALID_TOKEN_MESSAGE
            ))
            .thenApply(user -> new AuthenticationOutResource(user, tokenService.refresh(user, token)));
    }
}
