package com.ss.jcrm.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ss.jcrm.base.utils.WithId;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum EmailType implements WithId {
    WORK(1, "Work"),
    HOME(2, "Home");

    @Getter(onMethod_ = @JsonValue)
    private final long id;
    private final @Getter String name;

    private static final EmailType[] ID_TO_EMAIL_TYPE;
    private static final Map<String, EmailType> NAME_TO_EMAIL_TYPE;

    static {

        var length = Stream.of(EmailType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_EMAIL_TYPE = new EmailType[length + 1];
        NAME_TO_EMAIL_TYPE = new HashMap<>();

        for (var type : EmailType.values()) {
            ID_TO_EMAIL_TYPE[(int) type.id] = type;
            NAME_TO_EMAIL_TYPE.put(type.name(), type);
        }
    }

    public static boolean isValid(@Nullable String name) {
        return StringUtils.isNotEmpty(name) && NAME_TO_EMAIL_TYPE.containsKey(name);
    }

    public static @NotNull EmailType of(@Nullable String name) {
        if (!isValid(name)) {
            throw new IllegalArgumentException("Unknown type: " + name);
        } else {
            return NAME_TO_EMAIL_TYPE.get(name);
        }
    }

    public static @Nullable EmailType of(int id) {

        if (id < 0 || id >= ID_TO_EMAIL_TYPE.length) {
            return null;
        }

        return ID_TO_EMAIL_TYPE[id];
    }

    public static @NotNull EmailType require(long id) {
        return require((int) id);
    }

    @JsonCreator
    public static @NotNull EmailType require(int id) {

        if (id < 0 || id >= ID_TO_EMAIL_TYPE.length) {
            throw new IllegalArgumentException("Email type id cannot be < 0 or > " + (ID_TO_EMAIL_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_EMAIL_TYPE[id], id, integer ->
            new IllegalStateException("Can't find email type by id " + integer));
    }
}
