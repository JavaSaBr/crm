package com.ss.jcrm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class ChangedPasswordTokenException extends TokenException {

    public ChangedPasswordTokenException(@NotNull Throwable cause) {
        super(cause);
    }

    public ChangedPasswordTokenException(@NotNull String message) {
        super(message);
    }
}
