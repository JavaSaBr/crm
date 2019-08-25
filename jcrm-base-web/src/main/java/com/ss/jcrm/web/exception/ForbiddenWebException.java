package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class ForbiddenWebException extends WebException {

    public ForbiddenWebException(int errorCode) {
        super(HttpStatus.FORBIDDEN.value(), errorCode);
    }

    public ForbiddenWebException(@NotNull String errorMessage) {
        super(HttpStatus.FORBIDDEN.value(), errorMessage, 0);
    }

    public ForbiddenWebException(@NotNull String errorMessage, int errorCode) {
        super(HttpStatus.FORBIDDEN.value(), errorMessage, errorCode);
    }
}
