package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.user.api.EmailConfirmation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public interface EmailConfirmationDao extends Dao<EmailConfirmation> {

    @NotNull EmailConfirmation create(@NotNull String code, @NotNull String email, @NotNull Instant expiration);

    @NotNull CompletableFuture<@NotNull EmailConfirmation> createAsync(
        @NotNull String code,
        @NotNull String email,
        @NotNull Instant expiration
    );

    @Nullable EmailConfirmation findByEmailAndCode(@NotNull String email, @NotNull String code);

    @NotNull CompletableFuture<@Nullable EmailConfirmation> findByEmailAndCodeAsync(
        @NotNull String email, @NotNull String code
    );

    boolean delete(long id);

    @NotNull CompletableFuture<@NotNull Boolean> deleteAsync(long id);
}
