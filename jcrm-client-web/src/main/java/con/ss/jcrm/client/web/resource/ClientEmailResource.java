package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.client.api.ClientEmail;
import crm.client.api.EmailType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientEmailResource(@Nullable String email, @Nullable String type) {

  public static @NotNull ClientEmailResource of(@NotNull String email, @NotNull EmailType emailType) {
    return new ClientEmailResource(email, emailType.description());
  }

  public static @NotNull ClientEmailResource from(@NotNull ClientEmail email) {
    return new ClientEmailResource(
        email.email(),
        email.type().description());
  }
}
