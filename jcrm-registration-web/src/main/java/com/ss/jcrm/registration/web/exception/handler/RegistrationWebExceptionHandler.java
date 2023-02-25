package com.ss.jcrm.registration.web.exception.handler;

import crm.security.web.exception.handler.SecurityWebExceptionHandler;
import crm.base.web.exception.handler.DefaultWebExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Order(RegistrationWebExceptionHandler.ORDER)
public class RegistrationWebExceptionHandler extends DefaultWebExceptionHandler implements WebExceptionHandler {

    static final int ORDER = SecurityWebExceptionHandler.ORDER - 1;

    @Override
    public int getOrder() {
        return ORDER;
    }

    protected @NotNull Mono<ServerResponse> buildError(@NotNull Throwable throwable) {
        return Mono.empty();
    }
}
