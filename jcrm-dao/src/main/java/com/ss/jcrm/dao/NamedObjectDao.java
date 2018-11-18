package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface NamedObjectDao<T> extends Dao<T> {

    @Nullable T findByName(@NotNull String name);

    @NotNull CompletableFuture<@Nullable T> findByNameAsync(@NotNull String name);
}
