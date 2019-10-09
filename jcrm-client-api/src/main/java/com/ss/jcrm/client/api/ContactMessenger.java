package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ContactMessenger {

    @NotNull ContactMessenger[] EMPTY_ARRAY = new ContactMessenger[0];

    @NotNull String getLogin();

    void setLogin(@NotNull String login);

    @NotNull MessengerType getType();

    void setType(@NotNull MessengerType type);
}
