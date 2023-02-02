package crm.user.api.impl;

import crm.user.api.EmailConfirmation;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(of = "id")
public record DefaultEmailConfirmation(
    long id,
    @NotNull String code,
    @NotNull String email,
    @NotNull Instant expiration) implements EmailConfirmation {}
