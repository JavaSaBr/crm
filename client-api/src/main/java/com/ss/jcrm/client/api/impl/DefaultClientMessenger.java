package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientMessenger;
import com.ss.jcrm.client.api.MessengerType;
import org.jetbrains.annotations.NotNull;

public record DefaultClientMessenger(@NotNull String login, @NotNull MessengerType type)
    implements ClientMessenger {}
