package com.ss.jcrm.user.api;

import com.ss.jcrm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface EmailConfirmation extends UniqEntity {

    @NotNull String getCode();

    @NotNull String getEmail();

    @NotNull Instant getExpiration();
}
