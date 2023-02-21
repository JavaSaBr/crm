package crm.dao;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface Dao<T extends UniqEntity> {

  @NotNull Mono<T> findById(long id);

  /**
   * throws ObjectNotFoundDaoException if entity doesn't exist
   */
  @NotNull Mono<T> requireById(long id);

  default @NotNull Mono<Boolean> deleteById(long id) {
    throw new UnsupportedOperationException();
  }
}
