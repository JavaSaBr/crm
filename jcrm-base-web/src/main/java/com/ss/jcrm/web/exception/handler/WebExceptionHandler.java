package com.ss.jcrm.web.exception.handler;

import com.ss.jcrm.web.exception.WebException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(WebException.class)
    protected @NotNull ResponseEntity<?> handle(@NotNull WebException ex) {

        var status = ex.getStatus() == 0 ? HttpStatus.INTERNAL_SERVER_ERROR.value() : ex.getStatus();

        return ResponseEntity.status(status)
            .body(ex.getMessage());
    }
}
