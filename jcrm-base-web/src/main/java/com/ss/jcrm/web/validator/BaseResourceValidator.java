package com.ss.jcrm.web.validator;

import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

@Log4j2
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
        if (StringUtils.isNotEmpty(string) && string.length() > maxLength) {
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
            log.warn("Invalid email: {}", email);
            throw new BadRequestWebException(message, code);
        }
    }

    protected <T> void validateResultString(
        T @Nullable [] objects,
        int minLength,
        int maxLength,
        @NotNull Function<@NotNull T, @Nullable String> firstGetter,
        @NotNull Function<@NotNull T, @Nullable String> secondGetter,
        @NotNull Function<@NotNull T, @Nullable String> thirdGetter,
        int code,
        @NotNull String message
    ) {
        if (objects == null || objects.length < 1) {
            return;
        }

        for (var object : objects) {

            var first = firstGetter.apply(object);
            var second = secondGetter.apply(object);
            var third = thirdGetter.apply(object);

            if (StringUtils.isEmpty(first) || StringUtils.isEmpty(second) || StringUtils.isEmpty(third)) {
                throw new BadRequestWebException(message, code);
            }

            var resultLength = first.length() + second.length() + third.length();

            if (resultLength < minLength || resultLength > maxLength) {
                throw new BadRequestWebException(message, code);
            }
        }
    }

    protected <T> void validateField(
        T @Nullable [] objects,
        int minLength,
        int maxLength,
        @NotNull Function<@NotNull T, @Nullable String> getter,
        int code,
        @NotNull String message
    ) {
        if (objects == null || objects.length < 1) {
            return;
        }

        for (var object : objects) {
            var first = getter.apply(object);
            if (StringUtils.isEmpty(first) || first.length() < minLength || first.length() > maxLength) {
                throw new BadRequestWebException(message, code);
            }
        }
    }

    protected <T> void validateFields(
        T @Nullable [] objects,
        int minLength,
        int maxLength,
        @NotNull Function<@NotNull T, @Nullable String> firstGetter,
        @NotNull Function<@NotNull T, @Nullable String> secondGetter,
        @NotNull Function<@NotNull T, @Nullable String> thirdGetter,
        int code,
        @NotNull String message
    ) {
        if (objects == null || objects.length < 1) {
            return;
        }

        for (var object : objects) {
            var first = firstGetter.apply(object);
            var second = secondGetter.apply(object);
            var third = thirdGetter.apply(object);
            if (StringUtils.isEmpty(first) || first.length() < minLength || first.length() > maxLength) {
                throw new BadRequestWebException(message, code);
            } else if (StringUtils.isEmpty(second) || second.length() < minLength || second.length() > maxLength) {
                throw new BadRequestWebException(message, code);
            } else if (StringUtils.isEmpty(third) || third.length() < minLength || third.length() > maxLength) {
                throw new BadRequestWebException(message, code);
            }
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

    protected <T, F> void validateSameFields(
        @Nullable T[] objects,
        @NotNull Function<T, F> firstGetter,
        @NotNull Function<T, F> secondGetter,
        @NotNull Function<T, F> thirdGetter,
        @NotNull Predicate<F> validator,
        int code,
        @NotNull String message
    ) {
        if (objects == null || objects.length < 1) {
            return;
        }

        for (T object : objects) {
            if (!validator.test(firstGetter.apply(object))) {
                throw new BadRequestWebException(message, code);
            } else if (!validator.test(secondGetter.apply(object))) {
                throw new BadRequestWebException(message, code);
            } else if (!validator.test(thirdGetter.apply(object))) {
                throw new BadRequestWebException(message, code);
            }
        }
    }
}
