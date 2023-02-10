package crm.client.api.impl;

import crm.client.api.ClientEmail;
import crm.client.api.EmailType;
import org.jetbrains.annotations.NotNull;

public record DefaultClientEmail(@NotNull String email, @NotNull EmailType type)
    implements ClientEmail {}
