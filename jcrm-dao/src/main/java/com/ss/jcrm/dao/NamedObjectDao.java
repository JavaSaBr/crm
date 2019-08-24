package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface NamedObjectDao<T extends NamedEntity> extends Dao<T> {

    @NotNull Mono<@Nullable T> findByName(@NotNull String name);
}
