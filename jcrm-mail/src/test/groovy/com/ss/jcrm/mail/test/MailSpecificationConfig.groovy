package com.ss.jcrm.mail.test

import com.ss.jcrm.mail.MailConfig
import com.ss.jcrm.mail.service.impl.JavaxMailService
import com.ss.rlib.mail.sender.MailSenderConfig
import com.ss.rlib.testcontainers.FakeSMTPTestContainer
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import(MailConfig)
@Configuration
@PropertySource("classpath:com/ss/jcrm/mail/test/mail-test.properties")
class MailSpecificationConfig {

    @Bean
    @NotNull FakeSMTPTestContainer fakeSMTPContainer() {

        def container = new FakeSMTPTestContainer()
        container.start()
        container.waitForReadyState()

        return container
    }

    @Bean
    @NotNull JavaxMailService mailService() {

        def container = fakeSMTPContainer()

        def host = 'localhost'
        def port = container.getSmtpPort()
        def username = container.getSmtpUser()
        def password = container.getSmtpPassword()
        def smtpFrom = 'test@test.test'

        def config = MailSenderConfig.builder()
            .enableTtls(false)
            .port(port)
            .host(host)
            .useAuth(true)
            .username(username)
            .password(password)
            .from(smtpFrom)
            .build()

        return new JavaxMailService(config)
    }
}