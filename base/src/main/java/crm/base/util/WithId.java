package crm.base.util;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface WithId {

  static long @NotNull [] toIds(@NotNull Collection<? extends WithId> collection) {
    return collection
        .stream()
        .mapToLong(WithId::id)
        .toArray();
  }

  long id();
}
