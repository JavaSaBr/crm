package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletionException;
import java.util.function.Predicate;

public class ExceptionUtils {

    public static <T> @NotNull T badRequest(
        @NotNull Throwable throwable,
        @NotNull Predicate<Throwable> expected,
        int errorCode
    ) {
        if (expected.test(throwable)) {
            throw new BadRequestWebException(errorCode);
        } else {
            throw new CompletionException(throwable);
        }
    }

    public static <T> @NotNull T badRequest(
        @NotNull Throwable throwable,
        @NotNull Predicate<Throwable> expected,
        int errorCode,
        @NotNull String message
    ) {
        if (expected.test(throwable)) {
            throw new BadRequestWebException(message, errorCode);
        } else {
            throw new CompletionException(throwable);
        }
    }

    public static <T> @NotNull T unauthorized(
        @NotNull Throwable throwable,
        @NotNull Predicate<Throwable> expected,
        int errorCode,
        @NotNull String message
    ) {
        if (expected.test(throwable)) {
            throw new UnauthorizedWebException(message, errorCode);
        } else {
            throw new CompletionException(throwable);
        }
    }
}
