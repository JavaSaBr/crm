package com.ss.jcrm.registration.web.validator;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.*;
import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource;
import com.ss.jcrm.web.validator.BaseResourceValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

public class ResourceValidator extends BaseResourceValidator {

    private final int emailMaxLength;
    private final int emailMinLength;

    private final int orgNameMaxLength;
    private final int orgNameMinLength;

    private final int phoneNumberMaxLength;
    private final int phoneNumberMinLength;

    private final int passwordMaxLength;
    private final int passwordMinLength;

    private final int otherUserNameMaxLength;
    private final int otherUserNameMinLength;

    public ResourceValidator(@NotNull Environment env) {
        this.emailMaxLength = env.getProperty("registration.web.email.max.length", Integer.class, 45);
        this.emailMinLength = env.getProperty("registration.web.email.min.length", Integer.class, 6);
        this.orgNameMaxLength = env.getProperty("registration.web.organization.name.max.length", Integer.class, 45);
        this.orgNameMinLength = env.getProperty("registration.web.organization.name.min.length", Integer.class, 6);
        this.phoneNumberMaxLength = env.getProperty("registration.web.phone.number.max.length", Integer.class, 10);
        this.phoneNumberMinLength = env.getProperty("registration.web.phone.number.min.length", Integer.class, 6);
        this.passwordMaxLength = env.getProperty("registration.web.password.max.length", Integer.class, 25);
        this.passwordMinLength = env.getProperty("registration.web.password.min.length", Integer.class, 6);
        this.otherUserNameMaxLength = env.getProperty("registration.web.other.user.name.max.length", Integer.class, 45);
        this.otherUserNameMinLength = env.getProperty("registration.web.other.user.name.min.length", Integer.class, 2);
    }

    public void validate(@NotNull OrganizationRegisterInResource resource) {

        validateOrgName(resource.getOrgName());
        validateEmail(resource.getEmail());
        validateNotBlank(resource.getActivationCode(), INVALID_ACTIVATION_CODE, INVALID_OTHER_NAME_MESSAGE);
        validateOtherName(resource.getFirstName());
        validateOtherName(resource.getSecondName());
        validateOtherName(resource.getThirdName());

        validate(
            resource.getPassword(),
            passwordMinLength,
            passwordMaxLength,
            INVALID_PASSWORD,
            INVALID_PASSWORD_MESSAGE
        );

        validate(
            resource.getPhoneNumber(),
            phoneNumberMinLength,
            phoneNumberMaxLength,
            INVALID_PHONE_NUMBER,
            INVALID_PHONE_NUMBER_MESSAGE
        );
    }

    public void validateOrgName(@Nullable String orgName) {
        validate(orgName, orgNameMinLength, orgNameMaxLength, ORG_NAME_WRONG_LENGTH, ORG_NAME_WRONG_LENGTH_MESSAGE);
    }

    public void validateOtherName(@Nullable String userName) {
        validateNullable(
            userName,
            otherUserNameMinLength,
            otherUserNameMaxLength,
            INVALID_OTHER_NAME,
            INVALID_OTHER_NAME_MESSAGE
        );
    }

    public void validateEmail(@Nullable String email) {
        validateEmail(email, emailMinLength, emailMaxLength, INVALID_EMAIL, INVALID_EMAIL_MESSAGE);
    }
}
