package com.ss.jcrm.dictionary.web.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @param <R> the type of entities resource.
 * @param <C> the type of collection of all resources.
 */
public interface CachedDictionaryService<R, C> {

    @NotNull C getAll();

    @Nullable R getById(long id);

    @Nullable R getByName(@NotNull String name);
}
