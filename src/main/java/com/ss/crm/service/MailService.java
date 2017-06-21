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

    /**
     * Get a thread local context to put some settings of a letter.
     *
     * @return the thread local context.
     */
    @NotNull
    ObjectDictionary<String, Object> threadLocalContext();

    /**
     * Send a letter to email.
     *
     * @param email the target email.
     * @param template the template.
     * @param context the context.
     */
    void sendMail(@NotNull String email, @NotNull String template,
                  @NotNull ObjectDictionary<String, Object> context);

    /**
     * Get a template by ID.
     *
     * @param id the template id.
     * @return the template or null.
     */
    @NotNull
    String template(@NotNull String id);
}
