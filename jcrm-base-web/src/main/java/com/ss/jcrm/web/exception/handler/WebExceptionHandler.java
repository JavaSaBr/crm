package com.ss.jcrm.web.exception.handler;

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
    @NotNull ResponseEntity<?> webException(@NotNull WebException ex) {
        return buildError(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
    }

    public @NotNull ResponseEntity<?> buildError(int errorCode, @NotNull String message) {
        return buildError(0, errorCode, message);
    }

    public @NotNull ResponseEntity<?> buildError(@NotNull HttpStatus status, int errorCode, @NotNull String message) {
        return buildError(status.value(), errorCode, message);
    }

    public @NotNull ResponseEntity<?> buildError(int status, int errorCode, @NotNull String message) {

        var finalStatus = status == 0 ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status;

        return ResponseEntity.status(finalStatus)
            .header(WebException.HEADER_ERROR_CODE, String.valueOf(errorCode))
            .header(WebException.HEADER_ERROR_MESSAGE, message)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body("{\n\t\"errorCode\": " + errorCode + ",\n\t\"errorMessage\": \"" + message + "\"\n}");
    }
}
