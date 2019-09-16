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
public enum SiteType implements HasId {
    WORK(1, "Work"),
    HOME(2, "Home");

    private final long id;
    private final String name;

    private static final SiteType[] ID_TO_SITE_TYPE;

    static {

        var length = Stream.of(SiteType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_SITE_TYPE = new SiteType[length + 1];

        for (var accessRole : SiteType.values()) {
            ID_TO_SITE_TYPE[(int) accessRole.id] = accessRole;
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

    public static @NotNull SiteType require(int id) {

        if (id < 0 || id >= ID_TO_SITE_TYPE.length) {
            throw new IllegalArgumentException("Site type id cannot be < 0 or > " + (ID_TO_SITE_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_SITE_TYPE[id], id, integer ->
            new IllegalStateException("Can't find site type by id " + integer));
    }
}
