package com.ss.jcrm.web.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ResponseUtils {

    public static @NotNull Mono<ServerResponse> lazyNotFound() {
        return Mono.fromSupplier(() -> ServerResponse.notFound()
                .build())
            .flatMap(Function.identity());
    }

    public static <R> @NotNull Mono<ServerResponse> created(@NotNull R resource) {
        return ServerResponse.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .syncBody(resource);
    }

    public static @NotNull Mono<ServerResponse> ok(@Nullable Void aVoid) {
        return ServerResponse.ok()
            .build();
    }

    public static <R> @NotNull Mono<ServerResponse> ok(@NotNull R resource) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .syncBody(resource);
    }

    public static @NotNull Mono<ServerResponse> empty(@NotNull HttpStatus status) {
        return ServerResponse.status(status)
            .build();
    }

    public static @NotNull Mono<ServerResponse> exist(boolean exist) {
        return ServerResponse.status(exist ? HttpStatus.OK : HttpStatus.NOT_FOUND)
            .build();
    }
}
