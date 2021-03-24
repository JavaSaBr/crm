package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ClientMessenger {

    @NotNull ClientMessenger[] EMPTY_ARRAY = new ClientMessenger[0];

    @NotNull String getLogin();

    void setLogin(@NotNull String login);

    @NotNull MessengerType getType();

    void setType(@NotNull MessengerType type);
}
