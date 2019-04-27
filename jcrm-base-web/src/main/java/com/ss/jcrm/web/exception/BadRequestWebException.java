package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class BadRequestWebException extends WebException {

    public BadRequestWebException(int errorCode) {
        super(HttpStatus.BAD_REQUEST.value(), errorCode);
    }

    public BadRequestWebException(@NotNull String errorMessage) {
        super(HttpStatus.BAD_REQUEST.value(), errorMessage, 0);
    }

    public BadRequestWebException(@NotNull String errorMessage, int errorCode) {
        super(HttpStatus.BAD_REQUEST.value(), errorMessage, errorCode);
    }
}
