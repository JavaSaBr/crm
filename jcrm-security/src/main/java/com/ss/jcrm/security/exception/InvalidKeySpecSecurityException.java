package com.ss.jcrm.security.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidKeySpecSecurityException extends SecurityException {

    public InvalidKeySpecSecurityException(@NotNull String message) {
        super(message);
    }

    public InvalidKeySpecSecurityException(@NotNull Throwable cause) {
        super(cause);
    }
}
