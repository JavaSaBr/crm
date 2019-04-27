package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class NotFoundWebException extends WebException {

    public NotFoundWebException(@NotNull String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
