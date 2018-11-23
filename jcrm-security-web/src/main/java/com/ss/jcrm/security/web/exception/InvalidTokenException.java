package com.ss.jcrm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidTokenException extends TokenException {

    public InvalidTokenException(@NotNull Throwable cause) {
        super(cause);
    }
}
