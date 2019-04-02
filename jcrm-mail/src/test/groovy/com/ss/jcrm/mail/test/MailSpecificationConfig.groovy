package com.ss.jcrm.mail.test

import com.ss.jcrm.integration.test.smtp.FakeSMTPContainer
import com.ss.jcrm.mail.MailConfig
import com.ss.jcrm.mail.service.impl.JavaxMailService
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
    FakeSMTPContainer fakeSMTPContainer() {

        def container = new FakeSMTPContainer()
        container.start()

        while (!container.isRunning()) {
            Thread.sleep(500)
        }

        return container
    }

    @Bean
    @NotNull JavaxMailService mailService() {

        def container = fakeSMTPContainer()

        def host = 'localhost'
        def port = container.getMappedPort(FakeSMTPContainer.SMTP_PORT)
        def smtpAuth = true
        def startTtls = false
        def sslTrust = 'localhost'
        def username = container.username
        def password = container.password
        def smtpFrom = 'test@test.test'

        int minThreads = 1
        int maxThreads = 1
        int keepAlive = 60

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
        )
    }
}