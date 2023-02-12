package crm.contact.api;

import crm.contact.api.impl.DefaultMessenger;
import org.jetbrains.annotations.NotNull;

public interface Messenger {
  @NotNull String login();
  @NotNull MessengerType type();

  static @NotNull Messenger of(@NotNull String login, @NotNull MessengerType type) {
    return new DefaultMessenger(login, type);
  }
}
