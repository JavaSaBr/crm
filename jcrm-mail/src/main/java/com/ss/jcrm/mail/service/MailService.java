package com.ss.jcrm.mail.service;

import com.ss.jcrm.mail.exception.MailException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface MailService {

    /**
     * @throws MailException if something was wrong.
     */
    void send(@NotNull String email, @NotNull String subject, @NotNull String content);


    /**
     * @throws CompletionException -> MailException if something was wrong.
     */
    @NotNull CompletableFuture<Void> sendAsync(@NotNull String email, @NotNull String subject, @NotNull String content);
}
