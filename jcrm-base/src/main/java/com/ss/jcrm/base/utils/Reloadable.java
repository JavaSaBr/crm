package com.ss.jcrm.base.utils;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Reloadable {

    void reload();

    @NotNull CompletableFuture<Void> reloadAsync();
}
