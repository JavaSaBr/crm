package com.ss.jcrm.security.web.token.exception;

import org.jetbrains.annotations.NotNull;

public class TokenExpiredException extends TokenException {

    public TokenExpiredException(@NotNull String message) {
        super(message);
    }
}
