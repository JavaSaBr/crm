package crm.mail.service;

import crm.mail.exception.MailException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface MailService {

  /**
   * @throws MailException if something was wrong.
   */
  @NotNull Mono<Void> send(@NotNull String email, @NotNull String subject, @NotNull String content);
}
