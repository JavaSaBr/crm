package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.user.api.dao.UserDao;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserHandler {

    private final UserDao userDao;
    private final ResourceValidator resourceValidator;

    public @NotNull Mono<ServerResponse> existByEmail(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("email"))
            .doOnNext(resourceValidator::validateEmail)
            .map(userDao::existByEmailAsync)
            .flatMap(Mono::fromFuture)
            .map(exist -> exist? HttpStatus.OK : HttpStatus.NOT_FOUND)
            .switchIfEmpty(Mono.just(HttpStatus.NOT_FOUND))
            .flatMap(exist -> ServerResponse.status(exist)
                .build());
    }
}
