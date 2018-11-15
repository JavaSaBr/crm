package com.ss.jcrm.jdbc.exception;

import org.jetbrains.annotations.NotNull;

public class DuplicateJdbcException extends JdbcException {

    public DuplicateJdbcException(@NotNull String message) {
        super(message);
    }
}
