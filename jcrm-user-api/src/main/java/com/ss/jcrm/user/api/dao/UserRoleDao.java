package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.user.api.UserRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface UserRoleDao {

    @NotNull UserRole create(@NotNull String name);

    @NotNull CompletableFuture<@NotNull UserRole> createAsync(@NotNull String name);

    @Nullable UserRole findByName(@NotNull String name);

    @NotNull CompletableFuture<@Nullable UserRole> findByNameAsync(@NotNull String name);

    @Nullable UserRole findById(long id);

    @NotNull CompletableFuture<@Nullable UserRole> findByIdAsync(long id);

    @NotNull UserRole requireById(long id);

    @NotNull CompletableFuture<@NotNull UserRole> requireByIdAsync(long id);

    @NotNull Set<UserRole> getAll();

    @NotNull CompletableFuture<@NotNull Set<UserRole>> getAllAsync();
}
