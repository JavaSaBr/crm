package com.ss.jcrm.web.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class ResourceIsAlreadyChangedWebException extends WebException {

    public ResourceIsAlreadyChangedWebException() {
        super(
            HttpStatus.CONFLICT.value(),
            CommonErrors.RESOURCE_IS_ALREADY_CHANFED_MESSAGE,
            CommonErrors.RESOURCE_IS_ALREADY_CHANFED
        );
    }

    public ResourceIsAlreadyChangedWebException(@NotNull Throwable cause) {
        super(
            HttpStatus.CONFLICT.value(),
            cause,
            CommonErrors.RESOURCE_IS_ALREADY_CHANFED_MESSAGE,
            CommonErrors.RESOURCE_IS_ALREADY_CHANFED
        );
    }
}
