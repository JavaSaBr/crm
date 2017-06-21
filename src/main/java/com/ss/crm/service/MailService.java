package com.ss.crm.service;

import com.ss.rlib.util.dictionary.ObjectDictionary;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a mail service.
 *
 * @author JavaSaBr
 */
public interface MailService {

    String VAR_APP_NAME = "${app-name}";
    String VAR_FIRST_NAME = "${first-name}";
    String VAR_SECOND_NAME = "${second-name}";
    String VAR_LOGIN = "${login}";
    String VAR_PASSWORD = "${password}";
    String VAR_LINK = "${link}";
    String VAR_SUPPORT_EMAIL = "${support-email}";
    String VAR_SUPPORT_PHONE_NUMBER = "${support-phone-number}";

    String SUBJECT = "Subject";

    @NotNull
    ObjectDictionary<String, Object> threadLocalContext();

    void sendMail(@NotNull String email, @NotNull String template,
                  @NotNull ObjectDictionary<String, Object> context);

    String template(@NotNull String id);
}
