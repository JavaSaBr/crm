package com.ss.jcrm.dictionary.api.impl;

import com.ss.jcrm.dictionary.api.Industry;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultIndustry(@NotNull String name, long id) implements Industry {

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultIndustry that = (DefaultIndustry) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
