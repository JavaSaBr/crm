package com.ss.jcrm.user.contact.api.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.MessengerType;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
public class MessengerResource {

    public static @NotNull MessengerResource from(@NotNull Messenger messenger) {
        return new MessengerResource(messenger.getLogin(), (int) messenger.getType().id());
    }

    @Nullable String login;
    int type;

    @JsonCreator
    public MessengerResource(
        @JsonProperty("login") @Nullable String login,
        @JsonProperty("type") int type
    ) {
        this.login = login;
        this.type = type;
    }

    public @NotNull Messenger toMessenger() {
        return new Messenger(login, MessengerType.require(type));
    }
}
