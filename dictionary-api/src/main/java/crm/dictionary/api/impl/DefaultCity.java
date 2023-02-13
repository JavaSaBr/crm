package crm.dictionary.api.impl;

import crm.dictionary.api.City;
import crm.dictionary.api.Country;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultCity(@NotNull String name, @NotNull Country country, long id) implements City {

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) {
      return true;
    } else if (another == null || getClass() != another.getClass()) {
      return false;
    }
    DefaultCity that = (DefaultCity) another;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
