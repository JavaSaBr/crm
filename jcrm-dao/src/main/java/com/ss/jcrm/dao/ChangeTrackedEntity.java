package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface ChangeTrackedEntity extends Entity {

    @NotNull Instant getCreated();

    @NotNull Instant getModified();

    void setModified(@NotNull Instant modified);
}
