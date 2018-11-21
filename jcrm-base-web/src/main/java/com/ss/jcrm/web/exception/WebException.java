package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;

public class WebException extends RuntimeException {

    public WebException() {
    }

    public WebException(@NotNull String message) {
        super(message);
    }

    public WebException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public WebException(@NotNull Throwable cause) {
        super(cause);
    }
}
