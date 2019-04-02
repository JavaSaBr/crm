package com.ss.jcrm.registration.web.controller;

import com.ss.jcrm.mail.service.MailService;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
public class EmailConfirmationController {

    private final EmailConfirmationDao emailConfirmationDao;
    private final TokenService tokenService;
    private final ResourceValidator resourceValidator;
    private final TemplateRegistry emailConfirmationTemplate;
    private final String emailConfirmationSubject;
    private final MailService mailService;

    private final int activateCodeLength;
    private final int emailConfirmationExpiration;

    public EmailConfirmationController(
        @NotNull EmailConfirmationDao emailConfirmationDao,
        @NotNull TokenService tokenService,
        @NotNull ResourceValidator resourceValidator,
        @NotNull TemplateRegistry emailConfirmationTemplate,
        @NotNull MailService mailService,
        @NotNull Environment env
    ) {
        this.emailConfirmationDao = emailConfirmationDao;
        this.tokenService = tokenService;
        this.resourceValidator = resourceValidator;
        this.emailConfirmationTemplate = emailConfirmationTemplate;
        this.mailService = mailService;
        this.emailConfirmationSubject = env.getRequiredProperty("registration.web.email.confirmation.email.subject");
        this.activateCodeLength = env.getRequiredProperty("registration.web.email.confirmation.activate.code.length", int.class);
        this.emailConfirmationExpiration = env.getRequiredProperty("registration.web.email.confirmation.expiration", int.class);
    }

    @GetMapping("/registration/email/confirmation/{email}")
    @NotNull CompletableFuture<ResponseEntity<?>> emailConfirmation(@NotNull @PathVariable String email) {

        resourceValidator.validateEmail(email);

        var activateCode = tokenService.generateActivateCode(activateCodeLength);
        var expiration = Instant.now()
            .plus(emailConfirmationExpiration, ChronoUnit.MINUTES);

        return emailConfirmationDao.createAsync(activateCode, email, expiration)
            .thenCompose(emailConfirmation -> sendEmail(email, activateCode))
            .thenApply(aVoid -> new ResponseEntity<>(HttpStatus.OK));
    }

    private @NotNull CompletionStage<Void> sendEmail(@NotNull String email, @NotNull String activateCode) {

        var template = StringUtils.replace(
            emailConfirmationTemplate.getTemplate(),
            "{email}", email,
            "{code}", activateCode
        );

        return mailService.sendAsync(email, emailConfirmationSubject, template);
    }
}
