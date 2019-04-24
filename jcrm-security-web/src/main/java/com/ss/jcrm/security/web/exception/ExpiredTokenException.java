package com.ss.jcrm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class ExpiredTokenException extends TokenException {

    public ExpiredTokenException(@NotNull String message) {
        super(message);
    }
}
