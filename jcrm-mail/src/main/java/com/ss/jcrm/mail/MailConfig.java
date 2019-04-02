package com.ss.jcrm.mail;

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
    @NotNull JavaxMailService mailService() {

        var host = env.getRequiredProperty("javax.mail.smtp.host");
        var port = env.getRequiredProperty("javax.mail.smtp.port", int.class);
        var smtpAuth = env.getProperty("javax.mail.smtp.auth", boolean.class, true);
        var startTtls = env.getProperty("javax.mail.smtp.starttls.enable", boolean.class, true);
        var sslTrust = env.getRequiredProperty("javax.mail.smtp.ssl.trust");
        var username = env.getRequiredProperty("javax.mail.username");
        var password = env.getRequiredProperty("javax.mail.password");
        var smtpFrom = env.getRequiredProperty("javax.mail.smtp.from");

        int minThreads = env.getProperty("javax.mail.executor.min.threads", int.class, 1);
        int maxThreads = env.getProperty("javax.mail.executor.max.threads", int.class, 4);
        int keepAlive = env.getProperty("javax.mail.executor.keep.alive", int.class, 120);

        return new JavaxMailService(
            host,
            port,
            smtpAuth,
            startTtls,
            sslTrust,
            username,
            password,
            smtpFrom,
            minThreads,
            maxThreads,
            keepAlive
        );
    }
}
