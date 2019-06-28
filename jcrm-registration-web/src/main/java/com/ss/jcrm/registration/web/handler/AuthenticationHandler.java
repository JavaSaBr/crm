package com.ss.jcrm.registration.web.handler;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS;
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS_MESSAGE;
import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.registration.web.resources.AuthenticationInResource;
import com.ss.jcrm.registration.web.resources.AuthenticationOutResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.exception.UnauthorizedWebException;
import com.ss.rlib.common.util.AsyncUtils;
import com.ss.rlib.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
public class AuthenticationHandler {

    private final TokenService tokenService;
    private final UserDao userDao;
    private final PasswordService passwordService;
    private final ResourceValidator resourceValidator;

    public @NotNull Mono<ServerResponse> authenticate(@NotNull ServerRequest request) {
        return request.bodyToMono(AuthenticationInResource.class)
            .doOnNext(resourceValidator::validate)
            .zipWhen(this::loadUserByLogin, this::authenticate)
            .switchIfEmpty(Mono.error(() -> new UnauthorizedWebException(
                INVALID_CREDENTIALS_MESSAGE,
                INVALID_CREDENTIALS
            )))
            .flatMap(resource -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(resource));
    }

    public @NotNull Mono<ServerResponse> authenticateByToken(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("token"))
            .zipWhen(token -> Mono.fromFuture(tokenService.findUserIfNotExpiredAsync(token)
                .exceptionally(AsyncUtils::skip)), AuthenticationOutResource::new)
            .switchIfEmpty(Mono.error(() -> new UnauthorizedWebException(
                RegistrationErrors.INVALID_TOKEN_MESSAGE,
                RegistrationErrors.INVALID_TOKEN
            )))
            .flatMap(resource -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(resource));
    }

    public @NotNull Mono<ServerResponse> refreshToken(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("token"))
            .zipWhen(token -> Mono.fromFuture(tokenService.findUserAsync(token)
                .exceptionally(AsyncUtils::skip)), this::refreshToken)
            .switchIfEmpty(Mono.error(() -> new UnauthorizedWebException(
                RegistrationErrors.INVALID_TOKEN_MESSAGE,
                RegistrationErrors.INVALID_TOKEN
            )))
            .flatMap(resource -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(resource));
    }

    private @NotNull AuthenticationOutResource refreshToken(@NotNull String token, @NotNull User user) {
        return new AuthenticationOutResource(tokenService.refresh(user, token), user);
    }

    private @NotNull AuthenticationOutResource authenticate(
        @NotNull AuthenticationInResource resource,
        @NotNull User user
    ) {

        var password = resource.getPassword();
        try {

            if (!passwordService.isCorrect(password, user.getSalt(), user.getPassword())) {
               throw new UnauthorizedWebException(
                    INVALID_CREDENTIALS_MESSAGE,
                    INVALID_CREDENTIALS
                );
            }

        } finally {
            Arrays.fill(password, ' ');
        }

        return new AuthenticationOutResource(user, tokenService.generateNewToken(user));
    }

    private @NotNull Mono<? extends @Nullable User> loadUserByLogin(@NotNull AuthenticationInResource resource) {

        var login = resource.getLogin();
        var asyncUser = StringUtils.isEmail(login) ?
            userDao.findByEmailAsync(login) : userDao.findByPhoneNumberAsync(login);

        return Mono.fromFuture(asyncUser);
    }
}