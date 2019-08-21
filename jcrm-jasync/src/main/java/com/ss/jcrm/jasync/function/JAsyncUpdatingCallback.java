package com.ss.jcrm.jasync.function;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncUpdatingCallback<D extends Dao<T>, T extends Entity> {

    @Nullable T handle(@NotNull D dao, @NotNull T entity);
}
