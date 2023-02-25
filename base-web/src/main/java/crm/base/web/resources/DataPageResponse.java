package crm.base.web.resources;

import java.util.List;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record DataPageResponse<T>(@NotNull List<T> resources, long totalSize) {

  public static <T, R> @NotNull DataPageResponse<R> from(
      long totalSize,
      @NotNull List<T> list,
      @NotNull Function<T, R> mapper) {
    return new DataPageResponse<>(list
        .stream()
        .map(mapper)
        .toList(), totalSize);
  }
}
