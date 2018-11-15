package com.ss.jcrm.jdbc.exception;

import org.jetbrains.annotations.NotNull;

public class NotRelevantVersionJdbcException extends JdbcException {

    public NotRelevantVersionJdbcException(@NotNull String message) {
        super(message);
    }
}
