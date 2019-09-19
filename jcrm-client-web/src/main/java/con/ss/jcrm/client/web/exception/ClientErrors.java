package con.ss.jcrm.client.web.exception;

public interface ClientErrors {

    int CONTACT_FIRST_NAME_INVALID_LENGTH = 2000;
    String CONTACT_FIRST_NAME_INVALID_LENGTH_MESSAGE = "First name has invalid length";

    int CONTACT_SECOND_NAME_INVALID_LENGTH = 2001;
    String CONTACT_SECOND_NAME_INVALID_LENGTH_MESSAGE = "Second name has invalid length";

    int CONTACT_ASSIGNER_NOT_PRESENTED = 2002;
    String CONTACT_ASSIGNER_NOT_PRESENTED_MESSAGE = "Assigner is not presented";

    int CONTACT_THIRD_NAME_TOO_LONG = 2003;
    String CONTACT_THIRD_NAME_TOO_LONG_MESSAGE = "Third name is too long";

    int CONTACT_COMPANY_TOO_LONG = 2004;
    String CONTACT_COMPANY_TOO_LONG_MESSAGE = "Company is too long";

    int CONTACT_BIRTHDAY_INVALID = 2005;
    String CONTACT_BIRTHDAY_INVALID_MESSAGE = "Birthday is invalid";

    int CONTACT_PHONE_NUMBER_INVALID = 2006;
    String CONTACT_PHONE_NUMBER_INVALID_MESSAGE = "Phone number is invalid";

    int CONTACT_EMAIL_INVALID = 2007;
    String CONTACT_EMAIL_INVALID_MESSAGE = "Email is invalid";

    int CONTACT_SITE_INVALID = 2008;
    String CONTACT_SITE_INVALID_MESSAGE = "Site is invalid";

    int CONTACT_MESSENGER_INVALID = 2009;
    String CONTACT_MESSENGER_INVALID_MESSAGE = "Messenger is invalid";
}
