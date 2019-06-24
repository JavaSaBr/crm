package com.ss.jcrm.registration.web.exception.handler;

import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.security.web.exception.*;
import com.ss.jcrm.web.exception.handler.BaseWebExceptionHandler;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RegistrationExceptionHandler {

    private final BaseWebExceptionHandler handler;

    @ExceptionHandler(ExpiredTokenException.class)
    @NotNull ResponseEntity<?> expiredTokenException() {
        return handler.buildError(
            HttpStatus.UNAUTHORIZED,
            RegistrationErrors.EXPIRED_TOKEN,
            RegistrationErrors.EXPIRED_TOKEN_MESSAGE
        );
    }

    @ExceptionHandler(MaxRefreshedTokenException.class)
    @NotNull ResponseEntity<?> maxRefreshedTokenException() {
        return handler.buildError(
            HttpStatus.UNAUTHORIZED,
            RegistrationErrors.MAX_REFRESHED_TOKEN,
            RegistrationErrors.MAX_REFRESHED_TOKEN_MESSAGE
        );
    }

    @ExceptionHandler(TokenException.class)
    @NotNull ResponseEntity<?> invalidtokenException() {
        return handler.buildError(
            HttpStatus.UNAUTHORIZED,
            RegistrationErrors.INVALID_TOKEN,
            RegistrationErrors.INVALID_TOKEN_MESSAGE
        );
    }
}
