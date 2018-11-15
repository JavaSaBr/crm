package com.ss.jcrm.jdbc.exception;

import org.jetbrains.annotations.NotNull;

public class CannotGetGeneratedKeysJdbcException extends JdbcException {

    public CannotGetGeneratedKeysJdbcException(@NotNull String message) {
        super(message);
    }
}
