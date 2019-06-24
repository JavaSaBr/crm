package com.ss.jcrm.jasync.function;

import com.github.jasync.sql.db.RowData;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface JAsyncComposeConverter<D extends Dao<T>, T extends Entity> {

    @NotNull CompletableFuture<@Nullable T> convert(@NotNull D dao, @NotNull RowData data);
}
