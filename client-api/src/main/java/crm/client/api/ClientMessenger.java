package crm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ClientMessenger {
  @NotNull String login();
  @NotNull MessengerType type();
}
