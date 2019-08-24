package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.mail.service.MailService;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.spring.base.template.TemplateRegistry;
import com.ss.jcrm.user.api.EmailConfirmation;
import com.ss.jcrm.user.api.dao.EmailConfirmationDao;
import com.ss.rlib.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
public class EmailConfirmationHandler {

    private final EmailConfirmationDao emailConfirmationDao;
    private final TokenService tokenService;
    private final ResourceValidator resourceValidator;
    private final TemplateRegistry emailConfirmationTemplate;
    private final MailService mailService;

    private final String emailConfirmationSubject;

    private final int activateCodeLength;
    private final int emailConfirmationExpiration;

    public @NotNull Mono<ServerResponse> emailConfirmation(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("email"))
            .doOnNext(resourceValidator::validateEmail)
            .flatMap(this::createEmailConfirmation)
            .flatMap(this::sendEmail)
            .flatMap(aVoid -> ServerResponse.ok()
                .build());
    }

    private @NotNull Mono<@NotNull EmailConfirmation> createEmailConfirmation(@NotNull String email) {

        var activateCode = tokenService.generateActivateCode(activateCodeLength);
        var expiration = Instant.now()
            .plus(emailConfirmationExpiration, ChronoUnit.MINUTES);

        return emailConfirmationDao.create(activateCode, email, expiration);
    }

    private @NotNull Mono<Void> sendEmail(@NotNull EmailConfirmation emailConfirmation) {

        var template = StringUtils.replace(
            emailConfirmationTemplate.getTemplate(),
            "{email}", emailConfirmation.getEmail(),
            "{code}", emailConfirmation.getCode()
        );

        return mailService.send(
            emailConfirmation.getEmail(),
            emailConfirmationSubject,
            template
        );
    }
}
