package com.ss.jcrm.security.web.service.impl;

import static com.ss.jcrm.security.web.exception.SecurityErrors.*;
import static com.ss.jcrm.web.exception.ExceptionUtils.toForbidden;
import static com.ss.jcrm.web.exception.ExceptionUtils.toUnauthorized;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import crm.user.api.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultWebRequestSecurityService implements WebRequestSecurityService {

    @NotNull TokenService tokenService;

    @Override
    public @NotNull Mono<@NotNull User> isAuthorized(@NotNull ServerRequest request, @NotNull AccessRole accessRole) {
        return tokenService.findUserIfNotExpired(request.headers()
            .header(HEADER_TOKEN)
            .stream()
            .findFirst()
            .orElseThrow(() -> toUnauthorized(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)))
            .filter(user -> user.roles().contains(accessRole))
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

    @Override
    public @NotNull Set<AccessRole> resolveAllRoles(@NotNull User user) {

        var ownedRoles = new HashSet<AccessRole>();

        resolveAllRoles(ownedRoles, user.roles());

        for (var group : user.groups()) {
            resolveAllRoles(ownedRoles, group.roles());
        }

        return ownedRoles;
    }

    private void resolveAllRoles(@NotNull Set<AccessRole> ownedRoles, @NotNull Set<AccessRole> roles) {

        if (roles.isEmpty()) {
            return;
        }

        for (var role : roles) {
            ownedRoles.add(role);
            Collections.addAll(ownedRoles, role.getSubRoles());
        }
    }

    private boolean hasOne(@NotNull AccessRole[] accessRoles, @NotNull User user) {

        var roles = resolveAllRoles(user);

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
