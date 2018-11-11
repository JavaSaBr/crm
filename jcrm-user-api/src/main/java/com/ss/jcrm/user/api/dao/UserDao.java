package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.user.api.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface UserDao {

    @Nullable User findByName(@NotNull String name);

    @NotNull CompletableFuture<@Nullable User> findByNameAsync(@NotNull String name);

    @Nullable User findById(long id);

    @NotNull CompletableFuture<@Nullable User> findByIdAsync(long id);

    @NotNull User requireById(long id);

    @NotNull CompletableFuture<@NotNull User> requireByIdAsync(long id);
}
