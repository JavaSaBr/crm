package com.ss.jcrm.web.validator;

import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.Nullable;

public class BaseResourceValidator {

    protected void validate(@Nullable String string, int minLength, int maxLength, int code) {
        if (StringUtils.isEmpty(string) ||
            string.length() < minLength ||
            string.length() > maxLength
        ) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validateNullable(@Nullable String string, int minLength, int maxLength, int code) {
        if (string != null && (string.length() < minLength || string.length() > maxLength)) {
            throw new BadRequestWebException(code);
        }
    }

    protected void validateEmail(@Nullable String email, int minLength, int maxLength, int code) {
        if (StringUtils.checkEmail(email) ||
            email.length() < minLength ||
            email.length() > maxLength
        ) {
            throw new BadRequestWebException(code);
        }
    }
}
