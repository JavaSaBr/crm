package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.user.api.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrganizationDao {

    @NotNull Organization create(@NotNull String name);

    @NotNull CompletableFuture<@NotNull Organization> createAsync(@NotNull String name);

    @Nullable Organization findByName(@NotNull String name);

    @NotNull CompletableFuture<@Nullable Organization> findByNameAsync(@NotNull String name);

    @Nullable Organization findById(long id);

    @NotNull CompletableFuture<@Nullable Organization> findByIdAsync(long id);

    @NotNull Organization requireById(long id);

    @NotNull CompletableFuture<@NotNull Organization> requireByIdAsync(long id);

    @NotNull List<Organization> getAll();

    @NotNull CompletableFuture<@NotNull List<Organization>> getAllAsync();
}
