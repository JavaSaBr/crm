package crm.contact.api.impl;

import crm.contact.api.Email;
import crm.contact.api.EmailType;
import org.jetbrains.annotations.NotNull;

public record DefaultEmail(@NotNull String email, @NotNull EmailType type) implements Email {}
