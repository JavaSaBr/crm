package com.ss.jcrm.user.contact.api;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class Messenger {

    private static final @NotNull Messenger[] EMPTY_ARRAY = new Messenger[0];

    @NotNull String login;
    @NotNull MessengerType type;
}
