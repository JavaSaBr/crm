package com.ss.jcrm.dictionary.web.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @param <C> the type of collection of all resources.
 * @param <R> the type of entities resource.
 */
public interface CachedDictionaryService<C, R> {

    @NotNull C getAll();

    @Nullable R getById(long id);

    @Nullable R getByName(@NotNull String name);
}
