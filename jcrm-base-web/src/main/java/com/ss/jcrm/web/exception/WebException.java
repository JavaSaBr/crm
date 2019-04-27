package com.ss.jcrm.web.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class WebException extends RuntimeException {

    public static final String HEADER_ERROR_CODE = "Error-Code";
    public static final String HEADER_ERROR_MESSAGE = "Error-Message";

    @Getter
    private final int status;

    @Getter
    private final int errorCode;

    public WebException(int status, int errorCode) {
        super("no description");
        this.status = status;
        this.errorCode = errorCode;
    }

    public WebException(int status, @NotNull String message, int errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public WebException(int status, @NotNull String message) {
        super(message);
        this.status = status;
        this.errorCode = 0;
    }
}
