package con.ss.jcrm.client.web.validator;

import crm.client.api.EmailType;
import crm.client.api.MessengerType;
import crm.client.api.PhoneNumberType;
import crm.client.api.SiteType;
import crm.base.web.exception.BadRequestWebException;
import crm.base.web.validator.BaseResourceValidator;
import com.ss.rlib.common.util.StringUtils;
import con.ss.jcrm.client.web.exception.ClientErrors;
import con.ss.jcrm.client.web.resource.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.Month;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceValidator extends BaseResourceValidator {

  int clientNameMinLength;
  int clientNameMaxLength;
  int clientCompanyMaxLength;
  int clientPhoneNumberMinLength;
  int clientPhoneNumberMaxLength;
  int clientSiteMaxLength;
  int clientMessengerMaxLength;
  int clientEmailMaxLength;

  @NotNull LocalDate clientMinBirthday;
  @NotNull LocalDate clientMaxBirthday;

  public ResourceValidator(@NotNull Environment env) {
    this.clientNameMinLength = env.getProperty("client.web.contact.name.min.length", int.class, 1);
    this.clientNameMaxLength = env.getProperty("client.web.contact.name.max.length", int.class, 45);
    this.clientCompanyMaxLength = env.getProperty("client.web.contact.company.max.length", int.class, 100);
    this.clientPhoneNumberMinLength = env.getProperty("client.web.contact.phone-number.min.length", int.class, 3);
    this.clientPhoneNumberMaxLength = env.getProperty("client.web.contact.phone-number.max.length", int.class, 15);
    this.clientSiteMaxLength = env.getProperty("client.web.contact.site.max.length", int.class, 100);
    this.clientMessengerMaxLength = env.getProperty("client.web.contact.messenger.max.length", int.class, 45);
    this.clientEmailMaxLength = env.getProperty("client.web.contact.email.max.length", int.class, 45);
    this.clientMinBirthday = LocalDate.of(env.getProperty("client.web.contact.birthday.min.year", int.class, 1800),
        Month.JANUARY,
        1);
    this.clientMaxBirthday = LocalDate.of(env.getProperty("client.web.contact.birthday.max.year", int.class, 2200),
        Month.JANUARY,
        1);
  }

  public void validate(@NotNull ClientInResource resource) {

    if (resource.assigner() == 0) {
      throw new BadRequestWebException(ClientErrors.CLIENT_ASSIGNER_NOT_PRESENTED_MESSAGE,
          ClientErrors.CLIENT_ASSIGNER_NOT_PRESENTED);
    }

    validate(resource.firstName(),
        clientNameMinLength,
        clientNameMaxLength,
        ClientErrors.CLIENT_FIRST_NAME_INVALID_LENGTH,
        ClientErrors.CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE);

    validate(resource.secondName(),
        clientNameMinLength,
        clientNameMaxLength,
        ClientErrors.CLIENT_SECOND_NAME_INVALID_LENGTH,
        ClientErrors.CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE);

    validateMaxNullable(resource.thirdName(),
        clientNameMaxLength,
        ClientErrors.CLIENT_THIRD_NAME_TOO_LONG,
        ClientErrors.CLIENT_THIRD_NAME_TOO_LONG_MESSAGE);

    validateMaxNullable(resource.company(),
        clientCompanyMaxLength,
        ClientErrors.CLIENT_COMPANY_TOO_LONG,
        ClientErrors.CLIENT_COMPANY_TOO_LONG_MESSAGE);

    validateNullable(resource.birthday(),
        clientMinBirthday,
        clientMaxBirthday,
        ClientErrors.CLIENT_BIRTHDAY_INVALID,
        ClientErrors.CLIENT_BIRTHDAY_INVALID_MESSAGE);

    validateField(resource.phoneNumbers(),
        ClientPhoneNumberResource::type,
        PhoneNumberType::exist,
        ClientErrors.CLIENT_PHONE_NUMBER_INVALID,
        ClientErrors.CLIENT_PHONE_NUMBER_INVALID_MESSAGE);

    validateResultString(resource.phoneNumbers(),
        clientPhoneNumberMinLength,
        clientPhoneNumberMaxLength,
        ClientPhoneNumberResource::countryCode,
        ClientPhoneNumberResource::regionCode,
        ClientPhoneNumberResource::phoneNumber,
        ClientErrors.CLIENT_PHONE_NUMBER_INVALID,
        ClientErrors.CLIENT_PHONE_NUMBER_INVALID_MESSAGE);

    validateField(resource.emails(),
        ClientEmailResource::type,
        EmailType::exist,
        ClientErrors.CLIENT_EMAIL_INVALID,
        ClientErrors.CLIENT_EMAIL_INVALID_MESSAGE);

    validateField(resource.emails(),
        1,
        clientEmailMaxLength,
        ClientEmailResource::email,
        ClientErrors.CLIENT_EMAIL_INVALID,
        ClientErrors.CLIENT_EMAIL_INVALID_MESSAGE);

    validateField(resource.emails(),
        ClientEmailResource::email,
        StringUtils::isValidEmail,
        ClientErrors.CLIENT_EMAIL_INVALID,
        ClientErrors.CLIENT_EMAIL_INVALID_MESSAGE);

    validateField(resource.messengers(),
        ClientMessengerResource::type,
        MessengerType::exist,
        ClientErrors.CLIENT_MESSENGER_INVALID,
        ClientErrors.CLIENT_MESSENGER_INVALID_MESSAGE);

    validateField(resource.messengers(),
        1,
        clientMessengerMaxLength,
        ClientMessengerResource::login,
        ClientErrors.CLIENT_MESSENGER_INVALID,
        ClientErrors.CLIENT_MESSENGER_INVALID_MESSAGE);

    validateField(resource.sites(),
        ClientSiteResource::type,
        SiteType::exist,
        ClientErrors.CLIENT_SITE_INVALID,
        ClientErrors.CLIENT_SITE_INVALID_MESSAGE);

    validateField(resource.sites(),
        1,
        clientSiteMaxLength,
        ClientSiteResource::url,
        ClientErrors.CLIENT_SITE_INVALID,
        ClientErrors.CLIENT_SITE_INVALID_MESSAGE);
  }
}
