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
public enum PhoneNumberType implements WithId {
    WORK(1, "Work"),
    MOBILE(2, "Mobile");

    @Getter(onMethod_ = @JsonValue)
    private final long id;
    private final @Getter String name;

    private static final PhoneNumberType[] ID_TO_PHONE_NUMBER_TYPE;
    private static final Map<String, PhoneNumberType> NAME_TO_PHONE_NUMBER_TYPE;

    static {

        var length = Stream.of(PhoneNumberType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_PHONE_NUMBER_TYPE = new PhoneNumberType[length + 1];
        NAME_TO_PHONE_NUMBER_TYPE = new HashMap<>();

        for (var type : PhoneNumberType.values()) {
            ID_TO_PHONE_NUMBER_TYPE[(int) type.id] = type;
            NAME_TO_PHONE_NUMBER_TYPE.put(type.name(), type);
        }
    }

    public static boolean isValid(@Nullable String name) {
        return StringUtils.isNotEmpty(name) && NAME_TO_PHONE_NUMBER_TYPE.containsKey(name);
    }

    public static @NotNull PhoneNumberType of(@Nullable String name) {
        if (!isValid(name)) {
            throw new IllegalArgumentException("Unknown type: " + name);
        } else {
            return NAME_TO_PHONE_NUMBER_TYPE.get(name);
        }
    }

    public static @Nullable PhoneNumberType of(int id) {

        if (id < 0 || id >= ID_TO_PHONE_NUMBER_TYPE.length) {
            return null;
        }

        return ID_TO_PHONE_NUMBER_TYPE[id];
    }

    public static @NotNull PhoneNumberType require(long id) {
        return require((int) id);
    }

    @JsonCreator
    public static @NotNull PhoneNumberType require(int id) {

        if (id < 0 || id >= ID_TO_PHONE_NUMBER_TYPE.length) {
            throw new IllegalArgumentException("Phone number type id cannot be < 0 or > " + (ID_TO_PHONE_NUMBER_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_PHONE_NUMBER_TYPE[id], id, integer ->
            new IllegalStateException("Can't find phone number type by id " + integer));
    }
}
