package com.ss.jcrm.web.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class BadRequestWebException extends WebException {

    @Getter
    private final int errorCode;

    public BadRequestWebException(int errorCode) {
        super("", HttpStatus.BAD_REQUEST.value());
        this.errorCode = errorCode;
    }

    public BadRequestWebException(@NotNull String errorMessage) {
        super(errorMessage, HttpStatus.BAD_REQUEST.value());
        this.errorCode = 0;
    }

    public BadRequestWebException(@NotNull String errorMessage, int errorCode) {
        super(errorMessage, HttpStatus.BAD_REQUEST.value());
        this.errorCode = errorCode;
    }
}
