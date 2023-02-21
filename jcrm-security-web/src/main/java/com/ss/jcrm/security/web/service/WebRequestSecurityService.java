package com.ss.jcrm.security.web.service;

import crm.security.AccessRole;
import crm.user.api.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface WebRequestSecurityService {

    String HEADER_TOKEN = "jcrm-access-token";

    @NotNull Mono<@NotNull User> isAuthorized(@NotNull ServerRequest request, @NotNull AccessRole accessRole);

    @NotNull Mono<@NotNull User> isAuthorized(@NotNull ServerRequest request, @NotNull AccessRole[] accessRoles);

    @NotNull Mono<@NotNull User> isAuthorized(@NotNull ServerRequest request);

    @NotNull Set<AccessRole> resolveAllRoles(@NotNull User user);
}
