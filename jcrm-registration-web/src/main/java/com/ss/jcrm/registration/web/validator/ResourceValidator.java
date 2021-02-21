package com.ss.jcrm.registration.web.validator;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.*;
import com.ss.jcrm.registration.web.resources.AuthenticationInResource;
import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource;
import com.ss.jcrm.registration.web.resources.UserGroupInResource;
import com.ss.jcrm.registration.web.resources.UserInResource;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.jcrm.web.validator.BaseResourceValidator;
import com.ss.rlib.common.util.DateUtils;
import com.ss.rlib.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

@Slf4j
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

    private final int userGroupNameMinLength;
    private final int userGroupNameMaxLength;

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
        this.userGroupNameMinLength = env.getProperty("registration.web.user.group.name.min.length", int.class, 6);
        this.userGroupNameMaxLength = env.getProperty("registration.web.user.group.name.max.length", int.class, 45);

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

        validatePassword(resource.getPassword());
    }

    public void validate(@NotNull OrganizationRegisterInResource resource) {
        validateOrgName(resource.getOrgName());
        validateEmail(resource.getEmail());
        validateNotBlank(resource.getActivationCode(), INVALID_ACTIVATION_CODE, INVALID_OTHER_NAME_MESSAGE);
        validateOtherName(resource.getFirstName());
        validateOtherName(resource.getSecondName());
        validateOtherName(resource.getThirdName());
        validatePassword(resource.getPassword());
        requirePhoneNumber(resource.getPhoneNumber());
    }

    public void validateOrgName(@Nullable String orgName) {
        validate(orgName, orgNameMinLength, orgNameMaxLength, ORG_NAME_WRONG_LENGTH, ORG_NAME_WRONG_LENGTH_MESSAGE);
    }

    public void validateUserGroupName(@Nullable String orgName) {
        validate(orgName, userGroupNameMinLength, userGroupNameMaxLength, ORG_NAME_WRONG_LENGTH, ORG_NAME_WRONG_LENGTH_MESSAGE);
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

    public void validatePassword(@Nullable char[] password) {
        validate(password, passwordMinLength, passwordMaxLength, INVALID_PASSWORD, INVALID_PASSWORD_MESSAGE);
    }

    public void requirePhoneNumber(@Nullable PhoneNumberResource phoneNumber) {
        if (phoneNumber == null) {
            throw new BadRequestWebException(INVALID_PHONE_NUMBER_MESSAGE, INVALID_PHONE_NUMBER);
        } else {
            validatePhoneNumber(phoneNumber);
        }
    }

    public void validatePhoneNumber(@Nullable PhoneNumberResource phoneNumber) {

        if (phoneNumber == null) {
            return;
        }

        var countryCode = phoneNumber.getCountryCode();
        var regionCode = phoneNumber.getRegionCode();
        var number = phoneNumber.getPhoneNumber();

        if (countryCode == null || regionCode == null || number == null) {
            throw new BadRequestWebException(INVALID_PHONE_NUMBER_MESSAGE, INVALID_PHONE_NUMBER);
        }

        var resultLength = countryCode.length() + regionCode.length() + number.length();

        if (resultLength < phoneNumberMinLength || resultLength > phoneNumberMaxLength) {
            throw new BadRequestWebException(INVALID_PHONE_NUMBER_MESSAGE, INVALID_PHONE_NUMBER);
        }
    }

    public void validate(@NotNull UserInResource resource) {

        validateEmail(resource.getEmail());
        validatePassword(resource.getPassword());

        var birthday = DateUtils.toLocalDate(resource.getBirthday());

        if (birthday == null) {
            log.warn("Invalid birthday: {}", resource.getBirthday());
            throw new BadRequestWebException(INVALID_BIRTHDAY_MESSAGE, INVALID_BIRTHDAY);
        }
    }

    public void validate(@NotNull UserGroupInResource resource) {

        validateUserGroupName(resource.getName());
        validateAccessRoles(resource.getRoles());
    }

    public void validateAccessRoles(@Nullable int[] roles) {

        if (roles == null) {
            return;
        }

        for (int roleId : roles) {

            var accessRole = AccessRole.of(roleId);

            if (accessRole == null) {
                log.warn("Invalid access role id: {}", roleId);
                throw new BadRequestWebException(INVALID_ACCESS_ROLE_MESSAGE, INVALID_ACCESS_ROLE);
            }
        }
    }
}
