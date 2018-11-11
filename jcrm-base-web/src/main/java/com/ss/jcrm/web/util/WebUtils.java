package com.ss.jcrm.web.util;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import io.vertx.core.AsyncResult;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class WebUtils {

    public <T> @NotNull CompletableFuture<T> toCF(@NotNull AsyncResult<T> asyncResult) {
        if (asyncResult.succeeded()) {
            return completedFuture(asyncResult.result());
        } else {
            return failedFuture(asyncResult.cause());
        }
    }

    public <T> void apply(@NotNull AsyncResult<T> asyncResult, @NotNull CompletableFuture<T> future) {
        if (asyncResult.succeeded()) {
            future.complete(asyncResult.result());
        } else {
            future.completeExceptionally(asyncResult.cause());
        }
    }
}
