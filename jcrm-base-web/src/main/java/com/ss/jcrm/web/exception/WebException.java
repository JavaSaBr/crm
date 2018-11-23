package com.ss.jcrm.web.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class WebException extends RuntimeException {

    @Getter
    private final int status;

    public WebException() {
        this.status = 0;
    }

    public WebException(@NotNull String message) {
        super(message);
        this.status = 0;
    }

    public WebException(@NotNull String message, int status) {
        super(message);
        this.status = status;
    }

    public WebException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
        this.status = 0;
    }

    public WebException(@NotNull Throwable cause) {
        super(cause);
        this.status = 0;
    }
}
