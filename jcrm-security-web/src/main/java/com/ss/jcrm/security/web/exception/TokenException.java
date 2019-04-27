package com.ss.jcrm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class TokenException extends RuntimeException {

    public TokenException(@NotNull String message) {
        super(message);
    }

    public TokenException(@NotNull Throwable cause) {
        super(cause);
    }
}
