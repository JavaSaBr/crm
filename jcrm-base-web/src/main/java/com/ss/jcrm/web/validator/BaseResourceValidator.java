package com.ss.jcrm.web.validator;

import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class BaseResourceValidator {

    protected void validate(@Nullable String string, int minLength, int maxLength, int code) {
        if (StringUtils.isEmpty(string) ||
            string.length() < minLength ||
            string.length() > maxLength
        ) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validate(@Nullable String string, int minLength, int maxLength, int code, @NotNull String message) {
        if (StringUtils.isEmpty(string) ||
            string.length() < minLength ||
            string.length() > maxLength
        ) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validate(@Nullable char[] string, int minLength, int maxLength, int code) {
        if (string == null ||
            string.length < minLength ||
            string.length > maxLength
        ) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validate(@Nullable char[] string, int minLength, int maxLength, int code, @NotNull String message) {
        if (string == null ||
            string.length < minLength ||
            string.length > maxLength
        ) {
            if (string != null) Arrays.fill(string, ' ');
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validateNullable(@Nullable String string, int minLength, int maxLength, int code) {
        if (string != null && (string.length() < minLength || string.length() > maxLength)) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validateNullable(
        @Nullable String string,
        int minLength,
        int maxLength,
        int code,
        @NotNull String message
    ) {
        if (string != null && (string.length() < minLength || string.length() > maxLength)) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validateNotBlank(
        @Nullable String string,
        int code,
        @NotNull String message
    ) {
        if (string == null || string.isBlank()) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validateEmail(@Nullable String email, int minLength, int maxLength, int code) {
        if (email == null ||
            !StringUtils.checkEmail(email) ||
            email.length() < minLength ||
            email.length() > maxLength
        ) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validateEmail(
        @Nullable String email,
        int minLength,
        int maxLength,
        int code,
        @NotNull String message
    ) {
        if (email == null ||
            !StringUtils.checkEmail(email) ||
            email.length() < minLength ||
            email.length() > maxLength
        ) {
            throw new BadRequestWebException(message, code);
        }
    }
}
