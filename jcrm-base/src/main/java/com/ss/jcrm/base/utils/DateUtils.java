package com.ss.jcrm.base.utils;

import org.jetbrains.annotations.Nullable;

import java.time.*;

public class DateUtils {

    public static @Nullable OffsetDateTime toOffsetDateTime(@Nullable Instant instant) {
        return instant == null ? null : OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public static @Nullable Instant toUtcInstant(@Nullable LocalDateTime localDate) {
        return localDate == null ? null : localDate.toInstant(ZoneOffset.UTC);
    }

    public static @Nullable LocalDate toLocalDate(@Nullable LocalDateTime localDate) {
        return localDate == null ? null : localDate.toLocalDate();
    }
}
