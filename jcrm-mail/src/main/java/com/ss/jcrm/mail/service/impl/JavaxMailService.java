package com.ss.jcrm.mail.service.impl;

import com.ss.jcrm.mail.exception.MailException;
import com.ss.jcrm.mail.service.MailService;
import com.ss.rlib.mail.sender.MailSender;
import com.ss.rlib.mail.sender.MailSenderConfig;
import com.ss.rlib.mail.sender.exception.UncheckedMessagingException;
import com.ss.rlib.mail.sender.impl.JavaxMailSender;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

@Log4j2
public class JavaxMailService implements MailService {

    private final MailSender mailSender;

    public JavaxMailService(@NotNull MailSenderConfig config) {
        this.mailSender = new JavaxMailSender(config);
    }

    public JavaxMailService(
        @NotNull MailSenderConfig config,
        @NotNull JavaxMailSender.JavaxMailSenderConfig javaxConfig
    ) {
        this.mailSender = new JavaxMailSender(config, javaxConfig);
    }

    @Override
    public void send(@NotNull String email, @NotNull String subject, @NotNull String content) {
        try {
            mailSender.send(email, subject, content);
        } catch (UncheckedMessagingException e) {
            throw new MailException(e.getCause());
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> sendAsync(
        @NotNull String email,
        @NotNull String subject,
        @NotNull String content
    ) {
        return mailSender.sendAsync(email, subject, content);
    }
}
