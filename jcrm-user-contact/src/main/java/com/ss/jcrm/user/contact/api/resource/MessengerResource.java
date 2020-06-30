package com.ss.jcrm.user.contact.api.resource;

import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.MessengerType;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class MessengerResource {

    public static @NotNull MessengerResource from(@NotNull Messenger messenger) {
        return new MessengerResource(messenger.getLogin(), (int) messenger.getType().getId());
    }

    @NotNull String login;
    int type;

    public @NotNull Messenger toMessenger() {
        return new Messenger(login, MessengerType.require(type));
    }
}
