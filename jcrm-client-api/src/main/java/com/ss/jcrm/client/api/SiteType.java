package com.ss.jcrm.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ss.jcrm.base.utils.HasId;
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
public enum SiteType implements HasId {
    WORK(1, "Work"),
    HOME(2, "Home");

    @Getter(onMethod_ = @JsonValue)
    private final long id;
    private final @Getter String name;

    private static final SiteType[] ID_TO_SITE_TYPE;
    private static final Map<String, SiteType> NAME_TO_SITE_TYPE;

    static {

        var length = Stream.of(SiteType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_SITE_TYPE = new SiteType[length + 1];
        NAME_TO_SITE_TYPE = new HashMap<>();

        for (var type : SiteType.values()) {
            ID_TO_SITE_TYPE[(int) type.id] = type;
            NAME_TO_SITE_TYPE.put(type.name(), type);
        }
    }

    public static boolean isValid(@Nullable String name) {
        return StringUtils.isNotEmpty(name) && NAME_TO_SITE_TYPE.containsKey(name);
    }

    public static @NotNull SiteType of(@Nullable String name) {
        if (!isValid(name)) {
            throw new IllegalArgumentException("Unknown type: " + name);
        } else {
            return NAME_TO_SITE_TYPE.get(name);
        }
    }

    public static @Nullable SiteType of(int id) {

        if (id < 0 || id >= ID_TO_SITE_TYPE.length) {
            return null;
        }

        return ID_TO_SITE_TYPE[id];
    }

    public static @NotNull SiteType require(long id) {
        return require((int) id);
    }

    @JsonCreator
    public static @NotNull SiteType require(int id) {

        if (id < 0 || id >= ID_TO_SITE_TYPE.length) {
            throw new IllegalArgumentException("Site type id cannot be < 0 or > " + (ID_TO_SITE_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_SITE_TYPE[id], id, integer ->
            new IllegalStateException("Can't find site type by id " + integer));
    }
}
