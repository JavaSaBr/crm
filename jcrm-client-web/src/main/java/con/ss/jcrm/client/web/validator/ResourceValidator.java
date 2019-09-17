package con.ss.jcrm.client.web.validator;

import com.ss.jcrm.client.api.PhoneNumberType;
import com.ss.jcrm.web.exception.BadRequestWebException;
import com.ss.jcrm.web.validator.BaseResourceValidator;
import con.ss.jcrm.client.web.exception.ClientErrors;
import con.ss.jcrm.client.web.resource.ContactPhoneNumberResource;
import con.ss.jcrm.client.web.resource.CreateContactInResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.Month;

public class ResourceValidator extends BaseResourceValidator {

    private final int contactNameMinLength;
    private final int contactNameMaxLength;
    private final int contactCompanyMaxLength;

    private final LocalDate contactMinBirthday;
    private final LocalDate contactMaxBirthday;

    public ResourceValidator(@NotNull Environment env) {
        this.contactNameMinLength = env.getProperty("client.web.contact.name.min.length", int.class, 1);
        this.contactNameMaxLength = env.getProperty("client.web.contact.name.max.length", int.class, 45);
        this.contactCompanyMaxLength = env.getProperty("client.web.contact.company.max.length", int.class, 100);
        this.contactMinBirthday = LocalDate.of(
            env.getProperty("client.web.contact.birthday.min.year", int.class, 1800),
            Month.JANUARY,
            1
        );
        this.contactMaxBirthday = LocalDate.of(
            env.getProperty("client.web.contact.birthday.max.year", int.class, 2200),
            Month.JANUARY,
            1
        );
    }

    public void validate(@NotNull CreateContactInResource resource) {

        if (resource.getAssignerId() == 0) {
            throw new BadRequestWebException(
                ClientErrors.CONTACT_ASSIGNER_NOT_PRESENTED_MESSAGE,
                ClientErrors.CONTACT_ASSIGNER_NOT_PRESENTED
            );
        }

        validate(
            resource.getFirstName(),
            contactNameMinLength,
            contactNameMaxLength,
            ClientErrors.CONTACT_FIRST_NAME_INVALID_LENGTH,
            ClientErrors.CONTACT_FIRST_NAME_INVALID_LENGTH_MESSAGE
        );

        validate(
            resource.getSecondName(),
            contactNameMinLength,
            contactNameMaxLength,
            ClientErrors.CONTACT_SECOND_NAME_INVALID_LENGTH,
            ClientErrors.CONTACT_SECOND_NAME_INVALID_LENGTH_MESSAGE
        );

        validateMaxNullable(
            resource.getThirdName(),
            contactNameMaxLength,
            ClientErrors.CONTACT_THIRD_NAME_TOO_LONG,
            ClientErrors.CONTACT_THIRD_NAME_TOO_LONG_MESSAGE
        );

        validateMaxNullable(
            resource.getCompany(),
            contactCompanyMaxLength,
            ClientErrors.CONTACT_COMPANY_TOO_LONG,
            ClientErrors.CONTACT_COMPANY_TOO_LONG_MESSAGE
        );

        validateNullable(
            resource.getBirthday(),
            contactMinBirthday,
            contactMaxBirthday,
            ClientErrors.CONTACT_BIRTHDAY_INVALID,
            ClientErrors.CONTACT_BIRTHDAY_INVALID_MESSAGE
        );

        validateField(
            resource.getPhoneNumbers(),
            ContactPhoneNumberResource::getType,
            PhoneNumberType::isValid,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID_MESSAGE
        );

        validateField(
            resource.getPhoneNumbers(),
            ContactPhoneNumberResource::getType,
            PhoneNumberType::isValid,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID_MESSAGE
        );

        validateField(
            resource.getPhoneNumbers(),
            ContactPhoneNumberResource::getType,
            PhoneNumberType::isValid,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID_MESSAGE
        );

        validateField(
            resource.getPhoneNumbers(),
            ContactPhoneNumberResource::getType,
            PhoneNumberType::isValid,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID,
            ClientErrors.CONTACT_PHONE_NUMBER_INVALID_MESSAGE
        );

        //TODO
    }
}
