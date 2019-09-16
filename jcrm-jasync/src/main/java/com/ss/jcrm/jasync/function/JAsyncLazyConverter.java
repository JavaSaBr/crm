package com.ss.jcrm.jasync.function;

import com.github.jasync.sql.db.RowData;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface JAsyncLazyConverter<D extends Dao<T>, T extends UniqEntity> {

    @NotNull Mono<@Nullable T> convert(@NotNull D dao, @NotNull RowData data);
}
