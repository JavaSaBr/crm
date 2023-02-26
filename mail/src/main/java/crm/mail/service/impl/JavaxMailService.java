package crm.mail.service.impl;

import com.ss.rlib.mail.sender.impl.JavaxMailSender.JavaxMailSenderConfig;
import crm.mail.exception.MailException;
import crm.mail.service.MailService;
import com.ss.rlib.mail.sender.MailSender;
import com.ss.rlib.mail.sender.MailSenderConfig;
import com.ss.rlib.mail.sender.exception.UncheckedMessagingException;
import com.ss.rlib.mail.sender.impl.JavaxMailSender;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.concurrent.*;

public class JavaxMailService implements MailService {

  private final MailSender mailSender;

  public JavaxMailService(@NotNull MailSenderConfig config) {
    this.mailSender = new JavaxMailSender(config);
  }

  public JavaxMailService(@NotNull MailSenderConfig config, @NotNull JavaxMailSenderConfig javaxConfig) {
    this.mailSender = new JavaxMailSender(config, javaxConfig);
  }

  @Override
  public @NotNull Mono<Void> send(@NotNull String email, @NotNull String subject, @NotNull String content) {
    return Mono
        .fromFuture(() -> mailSender.sendAsync(email, subject, content))
        .onErrorResume(CompletionException.class::isInstance, ex -> Mono.error(ex.getCause()))
        .onErrorResume(
            UncheckedMessagingException.class::isInstance,
            ex -> Mono.error(new MailException(ex.getCause())));
  }
}
