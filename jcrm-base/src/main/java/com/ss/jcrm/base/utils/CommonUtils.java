package com.ss.jcrm.base.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

@UtilityClass
public class CommonUtils {

    private static final ThreadLocal<char[]> LOCAL_CHAR_BUFFER = ThreadLocal.withInitial(() -> new char[4096]);

    public @NotNull String toString(@NotNull Reader reader) {

        var buffer = LOCAL_CHAR_BUFFER.get();

        int length;
        try {
            length = reader.read(buffer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return String.valueOf(buffer, 0, length);
    }
}
