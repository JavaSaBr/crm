package crm.contact.api;

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
public enum EmailType implements EnumWithIdAndDescription {
  WORK(1, "Work"),
  HOME(2, "Home");

  private static final ExtendedEnumMap<EmailType> ENUM_MAP = new ExtendedEnumMap<>(EmailType.class);

  public static @Nullable EmailType withDescription(@Nullable String description) {
    return ENUM_MAP.withDescription(description);
  }

  public static @Nullable EmailType withId(int id) {
    return ENUM_MAP.withId(id);
  }

  public static @NotNull EmailType required(@NotNull String description) {
    return ENUM_MAP.required(description);
  }

  public static @NotNull EmailType required(long id) {
    return ENUM_MAP.required((int) id);
  }

  public static boolean exist(@Nullable String description) {
    return ENUM_MAP.exist(description);
  }

  public static boolean exist(long id) {
    return ENUM_MAP.exist(id);
  }

  @Getter(onMethod_ = @JsonValue)
  long id;

  @Getter
  String description;
}
