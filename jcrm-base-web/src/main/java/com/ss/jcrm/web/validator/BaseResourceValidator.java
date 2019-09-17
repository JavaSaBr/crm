package com.ss.jcrm.web.validator;

import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseResourceValidator {

    private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    protected void validate(@Nullable String string, int minLength, int maxLength, int code) {
        if (StringUtils.isEmpty(string) ||
            string.length() < minLength ||
            string.length() > maxLength
        ) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validateMin(@Nullable String string, int minLength, int code, @NotNull String message) {
        if (StringUtils.isEmpty(string) || string.length() < minLength) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validateMax(@Nullable String string, int maxLength, int code, @NotNull String message) {
        if (StringUtils.isEmpty(string) || string.length() > maxLength) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validateMaxNullable(@Nullable String string, int maxLength, int code, @NotNull String message) {
        if (StringUtils.isEmpty(string) && string.length() > maxLength) {
            throw new BadRequestWebException(message, code);
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

    protected void validate(
        @Nullable String string,
        @NotNull LocalDate minDate,
        @NotNull LocalDate maxDate,
        int code,
        @NotNull String message
    ) {

        if (StringUtils.isEmpty(string)) {
            throw new BadRequestWebException(message, code);
        }

        var date = Utils.tryGetAndConvert(string, ISO_LOCAL_DATE::parse, LocalDate::from);

        if (date == null || minDate.isAfter(date) || maxDate.isBefore(date)) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected void validateNullable(
        @Nullable String string,
        @NotNull LocalDate minDate,
        @NotNull LocalDate maxDate,
        int code,
        @NotNull String message
    ) {

        if (StringUtils.isEmpty(string)) {
            return;
        }

        var date = Utils.tryGetAndConvert(string, ISO_LOCAL_DATE::parse, LocalDate::from);

        if (date == null || minDate.isAfter(date) || maxDate.isBefore(date)) {
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
        if (!StringUtils.isEmpty(string) && (string.length() < minLength || string.length() > maxLength)) {
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
            !StringUtils.isValidEmail(email) ||
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
            !StringUtils.isValidEmail(email) ||
            email.length() < minLength ||
            email.length() > maxLength
        ) {
            throw new BadRequestWebException(message, code);
        }
    }

    protected <T, F> void validateField(
        @Nullable T[] objects,
        @NotNull Function<T, F> getter,
        @NotNull Predicate<F> validator,
        int code,
        @NotNull String message
    ) {
        if (objects == null || objects.length < 1) {
            return;
        }

        for (T object : objects) {
            if (!validator.test(getter.apply(object))) {
                throw new BadRequestWebException(message, code);
            }
        }
    }
}
