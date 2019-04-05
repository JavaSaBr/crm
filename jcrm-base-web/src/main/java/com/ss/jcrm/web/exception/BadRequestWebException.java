package com.ss.jcrm.web.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class BadRequestWebException extends WebException {

    public static final String HEADER_ERROR_CODE = "Error-Code";
    public static final String HEADER_ERROR_MESSAGE = "Error-Message";

    @Getter
    private final int errorCode;

    public BadRequestWebException(int errorCode) {
        super("no description", HttpStatus.BAD_REQUEST.value());
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
