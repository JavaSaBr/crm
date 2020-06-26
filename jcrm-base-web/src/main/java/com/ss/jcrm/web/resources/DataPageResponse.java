package com.ss.jcrm.web.resources;

import com.ss.rlib.common.util.array.Array;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.IntFunction;

@Value
public class DataPageResponse<T> {

    public static <T, R> @NotNull DataPageResponse<R> from(
        long totalSize,
        @NotNull Array<T> array,
        @NotNull Function<T, R> mapper,
        @NotNull IntFunction<R[]> generator
    ) {

        return new DataPageResponse<>(
            array.stream().map(mapper).toArray(generator),
            totalSize
        );
    }

    @NotNull T[] resources;

    long totalSize;
}
