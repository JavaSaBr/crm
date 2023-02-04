package crm.user.api.impl;

import com.ss.jcrm.security.AccessRole;
import crm.user.api.MinimalUser;

import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultMinimalUser(
    long id,
    long orgId,
    @NotNull String email,
    @NotNull Set<AccessRole> roles) implements MinimalUser {

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) {
      return true;
    } else if (another == null || getClass() != another.getClass()) {
      return false;
    }
    DefaultMinimalUser that = (DefaultMinimalUser) another;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}