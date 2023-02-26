package crm.dictionary.web.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @param <R> the type of entities resource.
 * @param <C> the type of collection of all resources.
 */
public interface CachedDictionaryService<R, C> {

  @NotNull C getAll();

  @Nullable R getById(long id);

  default @NotNull Optional<R> getByIdOptional(long id) {
    return Optional.ofNullable(getById(id));
  }

  @Nullable R getByName(@NotNull String name);

  default @NotNull Optional<R> getByNameOptional(@NotNull String name) {
    return Optional.ofNullable(getByName(name));
  }
}
