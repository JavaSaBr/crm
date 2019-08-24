package com.ss.jcrm.mail.service;

import com.ss.jcrm.mail.exception.MailException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface MailService {

    /**
     * @throws MailException if something was wrong.
     */
    @NotNull Mono<Void> send(@NotNull String email, @NotNull String subject, @NotNull String content);
}
