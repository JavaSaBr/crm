package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class BadRequestWebException extends WebException {

    public BadRequestWebException(@NotNull String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
