package com.ss.jcrm.user.contact.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ss.jcrm.base.utils.HasId;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MessengerType implements HasId {
    SKYPE(1, "Skype"),
    TELEGRAM(2, "Telegram"),
    WHATS_UP(3, "What's Up"),
    VIBER(4, "Viber");

    private static final MessengerType[] ID_TO_MESSENGER_TYPE;
    private static final Map<String, MessengerType> NAME_TO_MESSENGER_TYPE;

    static {

        var length = Stream.of(MessengerType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_MESSENGER_TYPE = new MessengerType[length + 1];
        NAME_TO_MESSENGER_TYPE = new HashMap<>();

        for (var type : MessengerType.values()) {
            ID_TO_MESSENGER_TYPE[(int) type.id] = type;
            NAME_TO_MESSENGER_TYPE.put(type.name(), type);
        }
    }

    public static boolean isValid(@Nullable String name) {
        return StringUtils.isNotEmpty(name) && NAME_TO_MESSENGER_TYPE.containsKey(name);
    }

    public static @NotNull MessengerType of(@Nullable String name) {
        if (!isValid(name)) {
            throw new IllegalArgumentException("Unknown type: " + name);
        } else {
            return NAME_TO_MESSENGER_TYPE.get(name);
        }
    }

    public static @Nullable MessengerType of(int id) {

        if (id < 0 || id >= ID_TO_MESSENGER_TYPE.length) {
            return null;
        }

        return ID_TO_MESSENGER_TYPE[id];
    }

    public static @NotNull MessengerType require(long id) {
        return require((int) id);
    }

    @JsonCreator
    public static @NotNull MessengerType require(int id) {

        if (id < 0 || id >= ID_TO_MESSENGER_TYPE.length) {
            throw new IllegalArgumentException("Messenger type id cannot be < 0 or > " + (ID_TO_MESSENGER_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_MESSENGER_TYPE[id], id, integer ->
            new IllegalStateException("Can't find messenger type by id " + integer));
    }

    @Getter(onMethod_ = @JsonValue)
    long id;

    @Getter String name;
}
