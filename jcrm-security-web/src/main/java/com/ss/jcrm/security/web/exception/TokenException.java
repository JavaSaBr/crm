package com.ss.jcrm.security.web.exception;

import com.ss.jcrm.web.exception.WebException;
import org.jetbrains.annotations.NotNull;

public class TokenException extends WebException {

    public TokenException(@NotNull String message) {
        super(message);
    }

    public TokenException(@NotNull Throwable cause) {
        super(cause);
    }
}
