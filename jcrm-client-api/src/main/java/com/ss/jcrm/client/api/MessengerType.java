package com.ss.jcrm.client.api;

import com.ss.jcrm.base.utils.HasId;
import com.ss.rlib.common.util.ObjectUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MessengerType implements HasId {
    SKYPE(1, "Skype"),
    TELEGRAM(2, "Telegram"),
    WHATSUP(3, "What's Up"),
    VIBER(4, "Viber");

    private final long id;
    private final String name;

    private static final MessengerType[] ID_TO_MESSENGER_TYPE;

    static {

        var length = Stream.of(MessengerType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_MESSENGER_TYPE = new MessengerType[length + 1];

        for (var accessRole : MessengerType.values()) {
            ID_TO_MESSENGER_TYPE[(int) accessRole.id] = accessRole;
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

    public static @NotNull MessengerType require(int id) {

        if (id < 0 || id >= ID_TO_MESSENGER_TYPE.length) {
            throw new IllegalArgumentException("Messenger type id cannot be < 0 or > " + (ID_TO_MESSENGER_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_MESSENGER_TYPE[id], id, integer ->
            new IllegalStateException("Can't find messenger type by id " + integer));
    }
}
