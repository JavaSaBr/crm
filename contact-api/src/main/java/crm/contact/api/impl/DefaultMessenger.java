package crm.contact.api.impl;

import crm.contact.api.Messenger;
import crm.contact.api.MessengerType;
import org.jetbrains.annotations.NotNull;

public record DefaultMessenger(@NotNull String login, @NotNull MessengerType type) implements Messenger {}
