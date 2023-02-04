package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface Dao<T extends UniqEntity> {

  @NotNull Mono<T> findById(long id);

  /**
   * throws ObjectNotFoundDaoException if entity doesn't exist
   */
  @NotNull Mono<T> requireById(long id);
}
