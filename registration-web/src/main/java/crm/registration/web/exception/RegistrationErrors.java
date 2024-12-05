package crm.registration.web.exception;

public interface RegistrationErrors {

  int COUNTRY_NOT_FOUND = 1000;
  String COUNTRY_NOT_FOUND_MESSAGE = "Unknown country";

  int ORG_ALREADY_EXIST = 1001;
  String ORG_ALREADY_EXIST_MESSAGE = "Organization is already exist";

  int ORG_NAME_WRONG_LENGTH = 1002;
  String ORG_NAME_WRONG_LENGTH_MESSAGE = "Invalid organization name";

  int INVALID_EMAIL = 1003;
  String INVALID_EMAIL_MESSAGE = "Invalid email";

  int EMAIL_ALREADY_EXIST = 1004;
  String EMAIL_ALREADY_EXIST_MESSAGE = "Email is already exist";

  int INVALID_PASSWORD = 1005;
  String INVALID_PASSWORD_MESSAGE = "Invalid password";

  int INVALID_PHONE_NUMBER = 1006;
  String INVALID_PHONE_NUMBER_MESSAGE = "Invalid phone number";

  int INVALID_OTHER_NAME = 1007;
  String INVALID_OTHER_NAME_MESSAGE = "Invalid name";

  int INVALID_ACTIVATION_CODE = 1008;
  String INVALID_ACTIVATION_CODE_MESSAGE = "Invalid activation code";

  int EMPTY_LOGIN = 1009;
  String EMPTY_LOGIN_MESSAGE = "Login is empty";

  int INVALID_CREDENTIALS = 1010;
  String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";

  int INVALID_BIRTHDAY = 1011;
  String INVALID_BIRTHDAY_MESSAGE = "Invalid birthday";

  int USER_GROUP_NAME_WRONG_LENGTH = 1012;
  String USER_GROUP_NAME_WRONG_LENGTH_MESSAGE = "Invalid user group name";

  int INVALID_ACCESS_ROLE = 1013;
  String INVALID_ACCESS_ROLE_MESSAGE = "Invalid access role";

  int USER_GROUP_IS_ALREADY_EXIST = 1014;
  String USER_GROUP_IS_ALREADY_EXIST_MESSAGE = "User group is already exist";

  int INVALID_USER_LIST = 1015;
  String INVALID_USER_LIST_MESSAGE = "Invalid user list";

  int USER_GROUP_IS_NOT_EXIST = 1016;
  String USER_GROUP_IS_NOT_EXIST_MESSAGE = "User group is not exist";

  int INVALID_PHONE_NUMBER_OR_EMAIL = 1017;
  String INVALID_PHONE_NUMBER_OR_EMAIL_MESSAGE = "Invalid phone number or email";
}
