package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.user.api.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserHandler {

    private final UserDao userDao;
    private final ResourceValidator resourceValidator;

    public @NotNull Mono<ServerResponse> existByEmail(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("email"))
            .doOnNext(resourceValidator::validateEmail)
            .flatMap(userDao::existByEmail)
            .map(exist -> exist? HttpStatus.OK : HttpStatus.NOT_FOUND)
            .flatMap(exist -> ServerResponse.status(exist)
                .build());
    }
}
