package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.registration.web.resources.UsersOutResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.web.resource.AuthorizedParam;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserHandler {

    private final UserDao userDao;
    private final ResourceValidator resourceValidator;
    private final WebRequestSecurityService webRequestSecurityService;

    public @NotNull Mono<ServerResponse> existByEmail(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("email"))
            .doOnNext(resourceValidator::validateEmail)
            .flatMap(userDao::existByEmail)
            .flatMap(ResponseUtils::exist);
    }

    public @NotNull Mono<ServerResponse> searchByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(res -> userDao.searchByName(res.getParam(), res.getOrgId()))
            .map(UsersOutResource::new)
            .flatMap(ResponseUtils::ok);
    }
}
