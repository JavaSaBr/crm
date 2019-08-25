package com.ss.jcrm.security;

import com.ss.jcrm.base.utils.HasId;
import com.ss.rlib.common.util.ObjectUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccessRole implements HasId {
    SUPER_ADMIN(1, "Super administrator"),
    ORG_ADMIN(2, "Organization administrator"),
    CURATOR(3, "Curator");

    private static final AccessRole[] ID_TO_ROLE;

    static {

        var length = Stream.of(AccessRole.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_ROLE = new AccessRole[length + 1];

        for (var accessRole : AccessRole.values()) {
            ID_TO_ROLE[(int) accessRole.id] = accessRole;
        }
    }

    public static @Nullable AccessRole of(int id) {

        if (id < 0 || id >= ID_TO_ROLE.length) {
            return null;
        }

        return ID_TO_ROLE[id];
    }

    public static @NotNull AccessRole require(long id) {
        return require((int) id);
    }

    public static @NotNull AccessRole require(int id) {

        if (id < 0 || id >= ID_TO_ROLE.length) {
            throw new IllegalArgumentException("An access role's id cannot be < 0 or > " + (ID_TO_ROLE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_ROLE[id], id, integer ->
            new IllegalStateException("Can't find an access role by the id " + integer));
    }

    private final long id;
    private final String name;
}
