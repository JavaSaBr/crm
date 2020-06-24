package com.ss.jcrm.registration.web.validator;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.*;
import com.ss.jcrm.registration.web.resources.AuthenticationInResource;
import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource;
import com.ss.jcrm.registration.web.resources.UserInResource;
import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.jcrm.web.validator.BaseResourceValidator;
import com.ss.rlib.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

@Log4j2
public class ResourceValidator extends BaseResourceValidator {

    private final int emailMinLength;
    private final int emailMaxLength;

    private final int orgNameMinLength;
    private final int orgNameMaxLength;

    private final int phoneNumberMinLength;
    private final int phoneNumberMaxLength;

    private final int passwordMinLength;
    private final int passwordMaxLength;

    private final int otherUserNameMinLength;
    private final int otherUserNameMaxLength;

    public ResourceValidator(@NotNull Environment env) {
        this.emailMinLength = env.getProperty("registration.web.email.min.length", int.class, 6);
        this.emailMaxLength = env.getProperty("registration.web.email.max.length", int.class, 45);
        this.orgNameMinLength = env.getProperty("registration.web.organization.name.min.length", int.class, 6);
        this.orgNameMaxLength = env.getProperty("registration.web.organization.name.max.length", int.class, 45);
        this.phoneNumberMinLength = env.getProperty("registration.web.phone.number.min.length", int.class, 6);
        this.phoneNumberMaxLength = env.getProperty("registration.web.phone.number.max.length", int.class, 10);
        this.passwordMinLength = env.getProperty("registration.web.password.min.length", int.class, 6);
        this.passwordMaxLength = env.getProperty("registration.web.password.max.length", int.class, 25);
        this.otherUserNameMinLength = env.getProperty("registration.web.other.user.name.min.length", int.class, 2);
        this.otherUserNameMaxLength = env.getProperty("registration.web.other.user.name.max.length", int.class, 45);

        log.info("Resource validator settings:");
        log.info("Email min length : {}", emailMinLength);
        log.info("Email max length : {}", emailMaxLength);
        log.info("Org name min length : {}", orgNameMinLength);
        log.info("Org name max length : {}", orgNameMaxLength);
        log.info("Phone number min length : {}", phoneNumberMinLength);
        log.info("Phone number max length : {}", phoneNumberMaxLength);
        log.info("Password min length : {}", passwordMinLength);
        log.info("Password max length : {}", passwordMaxLength);
        log.info("Other user name min length : {}", otherUserNameMinLength);
        log.info("Other user name max length :{}", otherUserNameMaxLength);
    }

    public void validate(@NotNull AuthenticationInResource resource) {

        var login = resource.getLogin();

        if (StringUtils.isEmpty(login)) {
            throw new BadRequestWebException(EMPTY_LOGIN_MESSAGE, EMPTY_LOGIN);
        } else if (StringUtils.isEmail(login)) {
            validateEmail(login);
        } else {
            validate(login,
                phoneNumberMinLength,
                phoneNumberMaxLength,
                INVALID_PHONE_NUMBER,
                INVALID_PHONE_NUMBER_MESSAGE
            );
        }

        validate(
            resource.getPassword(),
            passwordMinLength,
            passwordMaxLength,
            INVALID_PASSWORD,
            INVALID_PASSWORD_MESSAGE
        );
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

    public void validate(@NotNull UserInResource resource) {

    }
}
