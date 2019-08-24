package com.ss.jcrm.jasync.function;

import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface JAsyncCreationCallback<T extends Entity> {

    @Nullable T handle(long id);
}
