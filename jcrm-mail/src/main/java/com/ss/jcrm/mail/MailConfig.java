package com.ss.jcrm.mail;

import com.ss.jcrm.mail.service.impl.JavaxMailService;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.mail.sender.MailSenderConfig;
import com.ss.rlib.mail.sender.impl.JavaxMailSender;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/mail/mail.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/mail/mail-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
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

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(smtpFrom)) {
            throw new RuntimeException("SMTP username/password/from should not be null");
        }

        int minThreads = env.getProperty("javax.mail.executor.min.threads", int.class, 1);
        int maxThreads = env.getProperty("javax.mail.executor.max.threads", int.class, 4);
        int keepAlive = env.getProperty("javax.mail.executor.keep.alive", int.class, 120);

        var config = MailSenderConfig.builder()
            .sslHost(sslTrust)
            .enableTtls(startTtls)
            .port(port)
            .host(host)
            .useAuth(smtpAuth)
            .username(username)
            .password(password)
            .from(smtpFrom)
            .build();

        JavaxMailSender.JavaxMailSenderConfig javaxConfig = JavaxMailSender.JavaxMailSenderConfig.builder()
            .executorMaxThreads(maxThreads)
            .executorMinThreads(minThreads)
            .executorKeepAlive(keepAlive)
            .build();

        return new JavaxMailService(config, javaxConfig);
    }
}
