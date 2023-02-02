package com.ss.jcrm.base.utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommonUtils {

    public static long @NotNull [] toLongIds(@NotNull Collection<? extends WithId> objects) {
        return objects.stream()
            .mapToLong(WithId::id)
            .toArray();
    }

}
