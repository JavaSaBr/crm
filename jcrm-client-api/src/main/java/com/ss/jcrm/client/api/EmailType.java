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
public enum EmailType implements HasId {
    WORK(1, "Work"),
    HOME(2, "Home");

    private final long id;
    private final String name;

    private static final EmailType[] ID_TO_EMAIL_TYPE;

    static {

        var length = Stream.of(EmailType.values())
            .mapToInt(value -> (int) value.id)
            .max()
            .orElse(0);

        ID_TO_EMAIL_TYPE = new EmailType[length + 1];

        for (var accessRole : EmailType.values()) {
            ID_TO_EMAIL_TYPE[(int) accessRole.id] = accessRole;
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

    public static @NotNull EmailType require(int id) {

        if (id < 0 || id >= ID_TO_EMAIL_TYPE.length) {
            throw new IllegalArgumentException("Email type id cannot be < 0 or > " + (ID_TO_EMAIL_TYPE.length - 1));
        }

        return ObjectUtils.notNull(ID_TO_EMAIL_TYPE[id], id, integer ->
            new IllegalStateException("Can't find email type by id " + integer));
    }
}
