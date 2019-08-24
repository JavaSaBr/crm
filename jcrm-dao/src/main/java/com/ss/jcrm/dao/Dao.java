package com.ss.jcrm.dao;

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface Dao<T extends Entity> {

    @NotNull Mono<@Nullable T> findById(long id);

    /**
     * @throws ObjectNotFoundDaoException if an object with the id isn't exist.
     */
    @NotNull Mono<@NotNull T> requireById(long id);
}
