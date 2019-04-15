package com.ss.jcrm.registration.web.exception;

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
}
