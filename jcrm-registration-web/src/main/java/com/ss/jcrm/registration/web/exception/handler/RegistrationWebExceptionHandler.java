package com.ss.jcrm.registration.web.exception.handler;

import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.security.web.exception.ExpiredTokenException;
import com.ss.jcrm.security.web.exception.MaxRefreshedTokenException;
import com.ss.jcrm.security.web.exception.TokenException;
import com.ss.jcrm.web.exception.handler.DefaultWebExceptionHandler;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionException;

@Order(RegistrationWebExceptionHandler.ORDER)
@RestControllerAdvice
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RegistrationWebExceptionHandler extends DefaultWebExceptionHandler implements WebExceptionHandler {

    public static final int ORDER = DefaultWebExceptionHandler.ORDER - 1;

    @Override
    public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        return buildError(ex)
            .switchIfEmpty(Mono.error(ex))
            .flatMap(response -> response.writeTo(exchange, responseContext))
            .flatMap(aVoid -> Mono.empty());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    protected @NotNull Mono<ServerResponse> buildError(@NotNull Throwable throwable) {

        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }

        if (!(throwable instanceof TokenException)) {
            return Mono.empty();
        }

        if (throwable instanceof ExpiredTokenException) {
            return buildError(HttpStatus.UNAUTHORIZED,
                RegistrationErrors.EXPIRED_TOKEN,
                RegistrationErrors.EXPIRED_TOKEN_MESSAGE
            );
        } else if (throwable instanceof MaxRefreshedTokenException) {
            return buildError(HttpStatus.UNAUTHORIZED,
                RegistrationErrors.MAX_REFRESHED_TOKEN,
                RegistrationErrors.MAX_REFRESHED_TOKEN_MESSAGE
            );
        }

        return buildError(HttpStatus.UNAUTHORIZED,
            RegistrationErrors.INVALID_TOKEN,
            RegistrationErrors.INVALID_TOKEN_MESSAGE
        );
    }
}
