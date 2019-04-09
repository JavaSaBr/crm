package com.ss.jcrm.web.exception.handler;

import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.jcrm.web.exception.WebException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(WebException.class)
    protected @NotNull ResponseEntity<?> handle(@NotNull WebException ex) {

        var status = ex.getStatus() == 0 ?
            HttpStatus.INTERNAL_SERVER_ERROR.value() : ex.getStatus();

        return ResponseEntity.status(status)
            .body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestWebException.class)
    protected @NotNull ResponseEntity<?> handle(@NotNull BadRequestWebException ex) {

        var status = ex.getStatus() == 0 ?
            HttpStatus.INTERNAL_SERVER_ERROR.value() : ex.getStatus();

        return ResponseEntity.status(status)
            .header(BadRequestWebException.HEADER_ERROR_CODE, String.valueOf(ex.getErrorCode()))
            .header(BadRequestWebException.HEADER_ERROR_MESSAGE, ex.getMessage())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body("{\n\t\"errorCode\": " + ex.getErrorCode() + ",\n\t\"errorMessage\": \"" + ex.getMessage() + "\"\n}");
    }
}
