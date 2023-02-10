package crm.client.api.impl;

import crm.client.api.ClientMessenger;
import crm.client.api.MessengerType;
import org.jetbrains.annotations.NotNull;

public record DefaultClientMessenger(@NotNull String login, @NotNull MessengerType type)
    implements ClientMessenger {}
