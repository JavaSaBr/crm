package com.ss.jcrm.dao;

import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;

public record EntityPage<T extends Entity>(
    @NotNull Array<T> entities,
    long totalSize
) {}
