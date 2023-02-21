package com.ss.jcrm.registration.web.handler;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS;
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS_MESSAGE;
import static crm.base.web.util.ExceptionUtils.toUnauthorized;
import com.ss.jcrm.registration.web.resources.AuthenticationInResource;
import com.ss.jcrm.registration.web.resources.AuthenticationOutResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import crm.security.service.PasswordService;
import com.ss.jcrm.security.web.exception.SecurityErrors;
import com.ss.jcrm.security.web.service.TokenService;
import crm.user.api.User;
import crm.user.api.dao.UserDao;
import crm.base.web.exception.UnauthorizedWebException;
import crm.base.web.util.ResponseUtils;
import com.ss.rlib.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationHandler {

    @NotNull TokenService tokenService;
    @NotNull UserDao userDao;
    @NotNull PasswordService passwordService;
    @NotNull ResourceValidator resourceValidator;

    public @NotNull Mono<ServerResponse> authenticate(@NotNull ServerRequest request) {
        return request.bodyToMono(AuthenticationInResource.class)
            .doOnNext(resourceValidator::validate)
            .zipWhen(this::loadUserByLogin, this::authenticate)
            .switchIfEmpty(Mono.error(() -> toUnauthorized(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> authenticateByToken(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("token"))
            .zipWhen(tokenService::findUserIfNotExpired, AuthenticationOutResource::from)
            .switchIfEmpty(Mono.error(() -> toUnauthorized(
                SecurityErrors.INVALID_TOKEN,
                SecurityErrors.INVALID_TOKEN_MESSAGE
            )))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> refreshToken(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("token"))
            .zipWhen(tokenService::findUser, this::refreshToken)
            .switchIfEmpty(Mono.error(() -> toUnauthorized(
                SecurityErrors.INVALID_TOKEN,
                SecurityErrors.INVALID_TOKEN_MESSAGE
            )))
            .flatMap(ResponseUtils::ok);
    }

    private @NotNull AuthenticationOutResource refreshToken(@NotNull String token, @NotNull User user) {
        return AuthenticationOutResource.from(tokenService.refresh(user, token), user);
    }

    private @NotNull AuthenticationOutResource authenticate(
        @NotNull AuthenticationInResource resource, @NotNull User user
    ) {

        var password = resource.password();
        try {

            if (!passwordService.isCorrect(password, user.salt(), user.password())) {
                throw new UnauthorizedWebException(INVALID_CREDENTIALS_MESSAGE, INVALID_CREDENTIALS);
            }

        } finally {
            Arrays.fill(password, ' ');
        }

        return AuthenticationOutResource.from(tokenService.generateNewToken(user), user);
    }

    private @NotNull Mono<? extends @Nullable User> loadUserByLogin(@NotNull AuthenticationInResource resource) {
        var login = resource.login();
        return StringUtils.isEmail(login) ?
            userDao.findByEmail(login) : userDao.findByPhoneNumber(login);
    }
}
