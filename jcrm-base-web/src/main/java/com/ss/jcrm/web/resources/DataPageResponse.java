package com.ss.jcrm.web.resources;

import java.util.List;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.IntFunction;

public record DataPageResponse<T>(T @NotNull [] resources, long totalSize) {

  public static <T, R> @NotNull DataPageResponse<R> from(
      long totalSize,
      @NotNull List<T> list,
      @NotNull Function<T, R> mapper,
      @NotNull IntFunction<R[]> generator) {

    return new DataPageResponse<>(list
        .stream()
        .map(mapper)
        .toArray(generator), totalSize);
  }
}
