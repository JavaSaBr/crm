package crm.user.api.impl;

import crm.user.api.EmailConfirmation;
import java.util.Objects;

import java.time.Instant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultEmailConfirmation(
    long id,
    @NotNull String code,
    @NotNull String email,
    @NotNull Instant expiration) implements EmailConfirmation {

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) {
      return true;
    } else if (another == null || getClass() != another.getClass()) {
      return false;
    }
    DefaultEmailConfirmation that = (DefaultEmailConfirmation) another;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
