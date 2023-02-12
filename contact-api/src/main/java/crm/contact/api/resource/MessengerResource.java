package crm.contact.api.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.contact.api.Messenger;
import crm.contact.api.impl.DefaultMessenger;
import crm.contact.api.MessengerType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessengerResource(@Nullable String login, long type) {

  public static @NotNull MessengerResource of(@NotNull String login, @NotNull MessengerType type) {
    return new MessengerResource(login, type.id());
  }

  public static @NotNull List<MessengerResource> from(@NotNull Collection<Messenger> messengers) {
    return messengers
        .stream()
        .map(MessengerResource::from)
        .toList();
  }

  public static @NotNull MessengerResource from(@NotNull Messenger messenger) {
    return new MessengerResource(messenger.login(), messenger.type().id());
  }

  public static @NotNull Set<Messenger> toMessengers(@Nullable List<MessengerResource> resources) {
    return resources != null ? resources
        .stream()
        .map(MessengerResource::toMessenger)
        .collect(Collectors.toSet()) : Set.of();
  }

  public @NotNull Messenger toMessenger() {
    return new DefaultMessenger(Objects.requireNonNull(login), MessengerType.required(type));
  }
}
