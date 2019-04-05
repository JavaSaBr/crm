package com.ss.jcrm.registration.web.exception;

public interface RegistrationErrors {

    int COUNTRY_NOT_FOUND = 1000;
    int ORG_ALREADY_EXIST = 1001;
    int ORG_NAME_WRONG_LENGTH = 1002;

    int INVALID_EMAIL = 1003;
    String INVALID_EMAIL_MESSAGE = "Invalid email";

    int EMAIL_ALREADY_EXIST = 1004;
    int INVALID_PASSWORD = 1005;
    int INVALID_PHONE_NUMBER = 1006;
    int INVALID_OTHER_NAME = 1007;
}
