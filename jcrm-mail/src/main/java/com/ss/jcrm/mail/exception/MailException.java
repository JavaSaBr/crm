package com.ss.jcrm.mail.exception;

import org.jetbrains.annotations.NotNull;

public class MailException extends RuntimeException {

    public MailException(@NotNull Throwable cause) {
        super(cause);
    }
}
