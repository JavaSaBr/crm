package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ss.jcrm.client.api.ClientMessenger;
import com.ss.jcrm.client.api.MessengerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientMessengerResource(@Nullable String login, @Nullable String type) {

  public static @NotNull ClientMessengerResource of(@NotNull String login, @NotNull MessengerType type) {
    return new ClientMessengerResource(login, type.description());
  }

  public static @NotNull ClientMessengerResource from(@NotNull ClientMessenger messenger) {
    return new ClientMessengerResource(
        messenger.login(),
        messenger.type().description());
  }
}
