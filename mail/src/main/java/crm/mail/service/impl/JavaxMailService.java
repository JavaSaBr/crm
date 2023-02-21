package crm.mail.service.impl;

import crm.mail.exception.MailException;
import crm.mail.service.MailService;
import com.ss.rlib.mail.sender.MailSender;
import com.ss.rlib.mail.sender.MailSenderConfig;
import com.ss.rlib.mail.sender.exception.UncheckedMessagingException;
import com.ss.rlib.mail.sender.impl.JavaxMailSender;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

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
    public @NotNull Mono<Void> send(@NotNull String email, @NotNull String subject, @NotNull String content) {
        return Mono.fromFuture(mailSender.sendAsync(email, subject, content))
            .onErrorResume(CompletionException.class::isInstance, ex -> Mono.error(ex.getCause()))
            .onErrorResume(UncheckedMessagingException.class::isInstance, ex -> Mono.error(new MailException(ex.getCause())));
    }
}
