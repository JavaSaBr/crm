package com.ss.jcrm.mail;

import com.ss.jcrm.mail.service.MailService;
import com.ss.jcrm.mail.service.impl.JavaxMailService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    @NotNull MailService mailService() {
        return new JavaxMailService(env);
    }
}
