package com.ss.jcrm.security.web.service.impl;

import static com.ss.jcrm.security.web.exception.SecurityErrors.*;
import static com.ss.jcrm.web.exception.ExceptionUtils.toForbidden;
import static com.ss.jcrm.web.exception.ExceptionUtils.toUnauthorized;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.user.api.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultWebRequestSecurityService implements WebRequestSecurityService {

    private final TokenService tokenService;

    @Override
    public @NotNull Mono<@NotNull User> isAuthorized(@NotNull ServerRequest request, @NotNull AccessRole accessRole) {
        return tokenService.findUserIfNotExpired(request.headers()
            .header(HEADER_TOKEN)
            .stream()
            .findFirst()
            .orElseThrow(() -> toUnauthorized(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)))
            .filter(user -> user.getRoles().contains(accessRole))
            .switchIfEmpty(Mono.error(toForbidden(HAS_NO_REQUIRED_ACCESS_ROLE, HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE)));
    }

    @Override
    public @NotNull Mono<@NotNull User> isAuthorized(
        @NotNull ServerRequest request,
        @NotNull AccessRole[] accessRoles
    ) {

        if (accessRoles.length < 2) {
            throw new IllegalArgumentException("Access roles should be at least 2.");
        }

        return tokenService.findUserIfNotExpired(request.headers()
            .header(HEADER_TOKEN)
            .stream()
            .findFirst()
            .orElseThrow(() -> toUnauthorized(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)))
            .filter(user -> hasOne(accessRoles, user))
            .switchIfEmpty(Mono.error(toForbidden(HAS_NO_REQUIRED_ACCESS_ROLE, HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE)));
    }

    @Override
    public @NotNull Mono<@NotNull User> isAuthorized(@NotNull ServerRequest request) {
        return tokenService.findUserIfNotExpired(request.headers()
            .header(HEADER_TOKEN)
            .stream()
            .findFirst()
            .orElseThrow(() -> toUnauthorized(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)));
    }

    private boolean hasOne(@NotNull AccessRole[] accessRoles, @NotNull User user) {

        var roles = user.getRoles();

        if (roles.isEmpty()) {
            return false;
        } else if (accessRoles.length == 2) {
            return roles.contains(accessRoles[0]) || roles.contains(accessRoles[1]);
        } else if (accessRoles.length == 3) {
            return roles.contains(accessRoles[0]) || roles.contains(accessRoles[1]) || roles.contains(accessRoles[2]);
        }

        for (var accessRole : accessRoles) {
            if (roles.contains(accessRole)) {
                return true;
            }
        }

        return false;
    }
}
