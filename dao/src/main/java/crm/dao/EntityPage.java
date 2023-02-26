package crm.dao;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public record EntityPage<T extends Entity>(@NotNull List<T> entities, long totalSize) {

  private static final EntityPage EMPTY = new EntityPage(List.of(), 0);

  public static <T extends Entity> @NotNull EntityPage<T> empty() {
    return EMPTY;
  }

  public static <T extends Entity> @NotNull EntityPage<T> from(long totalSize, List<T> entities) {
    return new EntityPage<>(entities, totalSize);
  }
}
