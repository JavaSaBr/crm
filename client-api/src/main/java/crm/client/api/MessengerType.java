package crm.client.api;

import com.fasterxml.jackson.annotation.JsonValue;
import crm.base.util.EnumWithIdAndDescription;
import crm.base.util.ExtendedEnumMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MessengerType implements EnumWithIdAndDescription {
  SKYPE(1, "Skype"),
  TELEGRAM(2, "Telegram"),
  WHATS_UP(3, "What's Up"),
  VIBER(4, "Viber");

  private static final ExtendedEnumMap<MessengerType> ENUM_MAP = new ExtendedEnumMap<>(MessengerType.class);

  public static @Nullable MessengerType withDescription(@Nullable String description) {
    return ENUM_MAP.withDescription(description);
  }

  public static @Nullable MessengerType withId(int id) {
    return ENUM_MAP.withId(id);
  }

  public static @NotNull MessengerType required(@NotNull String description) {
    return ENUM_MAP.required(description);
  }

  public static @NotNull MessengerType required(long id) {
    return ENUM_MAP.required((int) id);
  }

  public static boolean exist(@Nullable String description) {
    return ENUM_MAP.exist(description);
  }

  @Getter(onMethod_ = @JsonValue)
  long id;

  @Getter
  String description;
}
