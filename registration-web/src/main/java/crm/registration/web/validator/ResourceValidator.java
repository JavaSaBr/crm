package crm.registration.web.validator;

import static crm.registration.web.exception.RegistrationErrors.*;

import crm.registration.web.resources.AuthenticationInResource;
import crm.registration.web.resources.OrganizationRegisterInResource;
import crm.registration.web.resources.UserGroupInResource;
import crm.registration.web.resources.UserInResource;
import crm.security.AccessRole;
import crm.base.web.exception.BadRequestWebException;
import crm.base.web.validator.BaseResourceValidator;
import com.ss.rlib.common.util.DateUtils;
import com.ss.rlib.common.util.StringUtils;
import crm.contact.api.resource.PhoneNumberResource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceValidator extends BaseResourceValidator {

  int emailMinLength;
  int emailMaxLength;

  int orgNameMinLength;
  int orgNameMaxLength;

  int phoneNumberMinLength;
  int phoneNumberMaxLength;

  int passwordMinLength;
  int passwordMaxLength;

  int otherUserNameMinLength;
  int otherUserNameMaxLength;

  int userGroupNameMinLength;
  int userGroupNameMaxLength;

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

    log.info("""
        Resource validator settings:
        Email min/max: [{}-{}], Org name min/max: [{}-{}] Phone number min/max: [{}-{}], 
        Password min/max" [{}-{}] Other user name min/max: [{}-{}]""",
        emailMinLength, emailMaxLength, orgNameMinLength, orgNameMaxLength,
        phoneNumberMinLength, phoneNumberMaxLength, passwordMinLength, passwordMaxLength,
        otherUserNameMinLength, otherUserNameMaxLength);
  }

  public void validate(@NotNull AuthenticationInResource resource) {

    var login = resource.login();

    if (StringUtils.isEmpty(login)) {
      throw new BadRequestWebException(EMPTY_LOGIN_MESSAGE, EMPTY_LOGIN);
    } else if (StringUtils.isEmail(login)) {
      validateEmail(login);
    } else {
      validate(login, phoneNumberMinLength, phoneNumberMaxLength, INVALID_PHONE_NUMBER_OR_EMAIL, INVALID_PHONE_NUMBER_OR_EMAIL_MESSAGE);
    }

    validatePassword(resource.password());
  }

  public void validate(@NotNull OrganizationRegisterInResource resource) {
    validateOrgName(resource.orgName());
    validateEmail(resource.email());
    validateNotBlank(resource.activationCode(), INVALID_ACTIVATION_CODE, INVALID_OTHER_NAME_MESSAGE);
    validateOtherName(resource.firstName());
    validateOtherName(resource.secondName());
    validateOtherName(resource.thirdName());
    validatePassword(resource.password());
    requirePhoneNumber(resource.phoneNumber());
  }

  public void validateOrgName(@Nullable String orgName) {
    validate(orgName, orgNameMinLength, orgNameMaxLength, ORG_NAME_WRONG_LENGTH, ORG_NAME_WRONG_LENGTH_MESSAGE);
  }

  public void validateUserGroupName(@Nullable String orgName) {
    validate(orgName,
        userGroupNameMinLength,
        userGroupNameMaxLength,
        ORG_NAME_WRONG_LENGTH,
        ORG_NAME_WRONG_LENGTH_MESSAGE);
  }

  public void validateOtherName(@Nullable String userName) {
    validateNullable(userName,
        otherUserNameMinLength,
        otherUserNameMaxLength,
        INVALID_OTHER_NAME,
        INVALID_OTHER_NAME_MESSAGE);
  }

  public void validateEmail(@Nullable String email) {
    validateEmail(email, emailMinLength, emailMaxLength, INVALID_EMAIL, INVALID_EMAIL_MESSAGE);
  }

  public void validatePassword(char @Nullable [] password) {
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

    var countryCode = phoneNumber.countryCode();
    var regionCode = phoneNumber.regionCode();
    var number = phoneNumber.phoneNumber();

    if (countryCode == null || regionCode == null || number == null) {
      throw new BadRequestWebException(INVALID_PHONE_NUMBER_MESSAGE, INVALID_PHONE_NUMBER);
    }

    var resultLength = countryCode.length() + regionCode.length() + number.length();

    if (resultLength < phoneNumberMinLength || resultLength > phoneNumberMaxLength) {
      throw new BadRequestWebException(INVALID_PHONE_NUMBER_MESSAGE, INVALID_PHONE_NUMBER);
    }
  }

  public void validate(@NotNull UserInResource resource) {

    validateEmail(resource.email());
    validatePassword(resource.password());

    var birthday = DateUtils.toLocalDate(resource.birthday());

    if (birthday == null) {
      log.warn("Invalid birthday: {}", resource.birthday());
      throw new BadRequestWebException(INVALID_BIRTHDAY_MESSAGE, INVALID_BIRTHDAY);
    }
  }

  public void validate(@NotNull UserGroupInResource resource) {

    validateUserGroupName(resource.name());
    validateAccessRoles(resource.roles());
  }

  public void validateAccessRoles(long @Nullable [] roles) {

    if (roles == null) {
      return;
    }

    for (var roleId : roles) {

      var accessRole = AccessRole.withId(roleId);

      if (accessRole == null) {
        log.warn("Invalid access role id: {}", roleId);
        throw new BadRequestWebException(INVALID_ACCESS_ROLE_MESSAGE, INVALID_ACCESS_ROLE);
      }
    }
  }
}
