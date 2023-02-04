package com.ss.jcrm.base.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class DateUtils {

  private static final ZoneOffset CURRENT_OFFSET = OffsetDateTime
      .now()
      .getOffset();

  public static @Nullable Instant toUtcInstant(@Nullable LocalDateTime localDate) {
    return localDate == null ? null : localDate.toInstant(CURRENT_OFFSET);
  }

  public static @Nullable LocalDate toLocalDate(@Nullable LocalDateTime localDate) {
    return localDate == null ? null : localDate.toLocalDate();
  }

  @Contract("!null->!null")
  public static @Nullable LocalDateTime toLocalDateTime(@Nullable Instant instant) {
    return instant == null ? null : LocalDateTime.ofInstant(instant, CURRENT_OFFSET);
  }
}
