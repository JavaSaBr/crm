package com.ss.jcrm.jdbc.exception;

import org.jetbrains.annotations.NotNull;

public class JdbcException extends RuntimeException {

    public JdbcException(@NotNull Throwable cause) {
        super(cause);
    }
}
