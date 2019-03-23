package com.ss.jcrm.web.exception;

import com.ss.jcrm.web.exception.WebException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class NotFoundWebException extends WebException {

    public NotFoundWebException(@NotNull String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
