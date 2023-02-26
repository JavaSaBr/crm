package crm.client.web.exception;

public interface ClientErrors {

  int CLIENT_FIRST_NAME_INVALID_LENGTH = 3000;
  String CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE = "First name has invalid length";

  int CLIENT_SECOND_NAME_INVALID_LENGTH = 3001;
  String CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE = "Second name has invalid length";

  int CLIENT_ASSIGNER_NOT_PRESENTED = 3002;
  String CLIENT_ASSIGNER_NOT_PRESENTED_MESSAGE = "Assigner is not presented";

  int CLIENT_THIRD_NAME_TOO_LONG = 3003;
  String CLIENT_THIRD_NAME_TOO_LONG_MESSAGE = "Third name is too long";

  int CLIENT_COMPANY_TOO_LONG = 3004;
  String CLIENT_COMPANY_TOO_LONG_MESSAGE = "Company is too long";

  int CLIENT_BIRTHDAY_INVALID = 3005;
  String CLIENT_BIRTHDAY_INVALID_MESSAGE = "Birthday is invalid";

  int CLIENT_PHONE_NUMBER_INVALID = 3006;
  String CLIENT_PHONE_NUMBER_INVALID_MESSAGE = "Phone number is invalid";

  int CLIENT_EMAIL_INVALID = 3007;
  String CLIENT_EMAIL_INVALID_MESSAGE = "Email is invalid";

  int CLIENT_SITE_INVALID = 3008;
  String CLIENT_SITE_INVALID_MESSAGE = "Site is invalid";

  int CLIENT_MESSENGER_INVALID = 3009;
  String CLIENT_MESSENGER_INVALID_MESSAGE = "Messenger is invalid";

  int INVALID_ASSIGNER = 3010;
  String INVALID_ASSIGNER_MESSAGE = "Assigner is not valid";
}
