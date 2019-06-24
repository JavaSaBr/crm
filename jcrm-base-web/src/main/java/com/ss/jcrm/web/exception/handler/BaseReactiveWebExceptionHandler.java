package com.ss.jcrm.web.exception.handler;

import com.ss.jcrm.web.exception.WebException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.List;

public class BaseReactiveWebExceptionHandler implements WebExceptionHandler {

    private final ServerResponse.Context responseContext;

    public BaseReactiveWebExceptionHandler() {
        this.responseContext = new HandlerStrategiesResponseContext(HandlerStrategies.withDefaults());
    }

    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        return buildError(ex)
            .flatMap(serverResponse -> serverResponse.writeTo(exchange, responseContext))
            .flatMap(aVoid -> Mono.empty());
    }

    protected @NotNull Mono<ServerResponse> buildError(@NotNull Throwable throwable) {
        if (throwable instanceof WebException) {
            var webException = (WebException) throwable;
            return buildError(webException.getStatus(), webException.getErrorCode(), webException.getMessage());
        } else {
            return Mono.empty();
        }
    }

    public @NotNull Mono<ServerResponse> buildError(int status, int errorCode, @NotNull String message) {
        return ServerResponse.status(status == 0 ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status)
            .header(WebException.HEADER_ERROR_CODE, String.valueOf(errorCode))
            .header(WebException.HEADER_ERROR_MESSAGE, message)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .syncBody("{\n\t\"errorCode\": " + errorCode + ",\n\t\"errorMessage\": \"" + message + "\"\n}");
    }

    @RequiredArgsConstructor
    protected static class HandlerStrategiesResponseContext implements ServerResponse.Context {

        private final HandlerStrategies strategies;

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return this.strategies.messageWriters();
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return this.strategies.viewResolvers();
        }
    }
}
