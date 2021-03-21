package com.ss.jcrm.base.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CommonUtils {

    public static long @NotNull [] toLongIds(@NotNull Collection<? extends HasId> objects) {
        return objects.stream()
            .mapToLong(HasId::getId)
            .toArray();
    }
}
