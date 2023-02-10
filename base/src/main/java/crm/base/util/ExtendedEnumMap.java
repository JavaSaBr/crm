package crm.base.util;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExtendedEnumMap<E extends EnumWithIdAndDescription> {

  @NotNull E[] idToEnum;
  @NotNull Map<String, E> nameToEnum;

  public ExtendedEnumMap(@NotNull Class<E> type) {

    E[] enumConstants = type.getEnumConstants();

    var length = Stream
        .of(enumConstants)
        .mapToInt(value -> (int) value.id())
        .max()
        .orElse(0);


    E[] idToEnum = ArrayUtils.create(type, length + 1);

    var nameToEnumMap = new HashMap<String, E>();

    for (var next : enumConstants) {
      idToEnum[(int) next.id()] = next;
      nameToEnumMap.put(next.description(), next);
    }

    this.idToEnum = Arrays.copyOf(idToEnum, idToEnum.length);
    this.nameToEnum = Map.copyOf(nameToEnumMap);
  }

  public @Nullable E withDescription(@Nullable String description) {
    return description == null ? null : nameToEnum.get(description);
  }

  public @Nullable E withId(int id) {

    if (id < 0 || id >= idToEnum.length) {
      return null;
    }

    return idToEnum[id];
  }

  public @NotNull E required(int id) {
    return ObjectUtils.notNull(withId(id));
  }

  public @NotNull E required(@NotNull String description) {
    return ObjectUtils.notNull(withDescription(description));
  }

  public boolean exist(@Nullable String description) {
    return StringUtils.isNotEmpty(description) && nameToEnum.containsKey(description);
  }
}
