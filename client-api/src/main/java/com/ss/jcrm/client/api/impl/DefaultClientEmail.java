package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientEmail;
import com.ss.jcrm.client.api.EmailType;
import org.jetbrains.annotations.NotNull;

public record DefaultClientEmail(@NotNull String email, @NotNull EmailType type)
    implements ClientEmail {}
