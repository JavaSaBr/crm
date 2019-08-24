package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.user.api.EmailConfirmation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public interface EmailConfirmationDao extends Dao<EmailConfirmation> {

    @NotNull Mono<@NotNull EmailConfirmation> create(
        @NotNull String code,
        @NotNull String email,
        @NotNull Instant expiration
    );

    @NotNull Mono<@Nullable EmailConfirmation> findByEmailAndCode(@NotNull String email, @NotNull String code);

    @NotNull Mono<Boolean> delete(long id);
}
