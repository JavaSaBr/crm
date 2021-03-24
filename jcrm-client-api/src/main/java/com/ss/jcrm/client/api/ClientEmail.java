package com.ss.jcrm.client.api;

import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;

public interface ClientEmail extends Entity {

    @NotNull ClientEmail[] EMPTY_ARRAY = new ClientEmail[0];

    @NotNull String getEmail();

    void setEmail(@NotNull String email);

    @NotNull EmailType getType();

    void setType(@NotNull EmailType type);
}
