package com.ss.jcrm.dao;

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

public interface Dao<T extends UniqEntity> {

    @NotNull Mono<@Nullable T> findById(long id);

    /**
     * @throws ObjectNotFoundDaoException if an object with the id isn't exist.
     */
    @NotNull Mono<@NotNull T> requireById(long id);
}
