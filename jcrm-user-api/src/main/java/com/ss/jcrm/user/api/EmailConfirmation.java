package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface EmailConfirmation extends Entity {

    @NotNull String getCode();

    @NotNull String getEmail();

    @NotNull Instant getExpiration();
}
