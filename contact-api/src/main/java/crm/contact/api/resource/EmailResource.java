package crm.contact.api.resource;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.contact.api.Email;
import crm.contact.api.EmailType;
import crm.contact.api.impl.DefaultEmail;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmailResource(@Nullable String email, long type) {

  public static @NotNull EmailResource of(@NotNull String email, @NotNull EmailType emailType) {
    return new EmailResource(email, emailType.id());
  }

  public static @NotNull List<EmailResource> from(@NotNull Collection<Email> emails) {
    return emails
        .stream()
        .map(EmailResource::from)
        .toList();
  }

  public static @NotNull EmailResource from(@NotNull Email email) {
    return new EmailResource(email.email(), email.type().id());
  }

  public static @NotNull Set<Email> toEmails(@Nullable Collection<EmailResource> resources) {
    return resources != null ? resources
        .stream()
        .map(EmailResource::toEmail)
        .collect(Collectors.toSet()) : Set.of();
  }

  public @NotNull Email toEmail() {
    return new DefaultEmail(notNull(email), EmailType.required(type));
  }
}
