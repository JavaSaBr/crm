package crm.dictionary.api.impl;

import crm.dictionary.api.Country;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultCountry(
    @NotNull String name,
    @NotNull String flagCode,
    @NotNull String phoneCode,
    long id) implements Country {

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) {
      return true;
    } else if (another == null || getClass() != another.getClass()) {
      return false;
    }
    var that = (DefaultCountry) another;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
