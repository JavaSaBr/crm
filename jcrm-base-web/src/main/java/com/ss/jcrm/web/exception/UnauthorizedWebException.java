package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class UnauthorizedWebException extends WebException {

    public UnauthorizedWebException(int errorCode) {
        super(HttpStatus.UNAUTHORIZED.value(), errorCode);
    }

    public UnauthorizedWebException(@NotNull String errorMessage) {
        super(HttpStatus.UNAUTHORIZED.value(), errorMessage, 0);
    }

    public UnauthorizedWebException(@NotNull String errorMessage, int errorCode) {
        super(HttpStatus.UNAUTHORIZED.value(), errorMessage, errorCode);
    }
}
