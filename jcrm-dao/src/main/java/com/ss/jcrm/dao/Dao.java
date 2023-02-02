package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface Dao<T extends UniqEntity> {

    @NotNull Mono<@NotNull T> findById(long id);
}
