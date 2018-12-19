package com.ss.jcrm.dao;

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface Dao<T extends Entity> {

    @Nullable T findById(long id);

    @NotNull CompletableFuture<@Nullable T> findByIdAsync(long id);

    /**
     * @throws ObjectNotFoundDaoException if an object with the id isn't exist.
     */
    @NotNull T requireById(long id);

    /**
     * @throws ObjectNotFoundDaoException if an object with the id isn't exist.
     */
    @NotNull CompletableFuture<@NotNull T> requireByIdAsync(long id);
}
