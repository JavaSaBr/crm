package com.ss.jcrm.security.web.exception.handler;

import static com.ss.jcrm.security.web.exception.SecurityErrors.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import com.ss.jcrm.security.web.exception.ExpiredTokenException;
import com.ss.jcrm.security.web.exception.MaxRefreshedTokenException;
import com.ss.jcrm.security.web.exception.TokenException;
import com.ss.jcrm.web.exception.handler.DefaultWebExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionException;

@Order(SecurityWebExceptionHandler.ORDER)
public class SecurityWebExceptionHandler extends DefaultWebExceptionHandler implements WebExceptionHandler {

    public static final int ORDER = DefaultWebExceptionHandler.ORDER - 1;

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
            return buildError(UNAUTHORIZED, EXPIRED_TOKEN, EXPIRED_TOKEN_MESSAGE);
        } else if (throwable instanceof MaxRefreshedTokenException) {
            return buildError(UNAUTHORIZED, MAX_REFRESHED_TOKEN, MAX_REFRESHED_TOKEN_MESSAGE);
        }

        return buildError(UNAUTHORIZED, INVALID_TOKEN, INVALID_TOKEN_MESSAGE);
    }
}
