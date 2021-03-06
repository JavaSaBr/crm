package com.ss.jcrm.jasync.function;

import com.github.jasync.sql.db.RowData;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncConverter<D extends Dao<T>, T extends UniqEntity> {

    @Nullable T convert(@NotNull D dao, @NotNull RowData data);
}
