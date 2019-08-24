package com.ss.jcrm.base.utils;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface Reloadable {

    void reload();

    @NotNull Mono<Void> reloadAsync();
}
