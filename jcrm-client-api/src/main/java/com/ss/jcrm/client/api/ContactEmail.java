package com.ss.jcrm.client.api;

import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;

public interface ContactEmail extends Entity {

    @NotNull String getEmail();

    void setEmail(@NotNull String email);

    @NotNull EmailType getType();

    void setType(@NotNull EmailType type);
}