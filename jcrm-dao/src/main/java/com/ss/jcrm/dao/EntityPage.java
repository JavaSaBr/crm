package com.ss.jcrm.dao;

import com.ss.rlib.common.util.array.Array;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EntityPage<T extends Entity> {

    private final @Getter Array<T> entities;
    private final @Getter long totalSize;
}
