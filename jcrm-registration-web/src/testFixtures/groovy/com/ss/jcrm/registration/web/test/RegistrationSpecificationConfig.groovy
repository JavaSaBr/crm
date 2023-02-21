package com.ss.jcrm.registration.web.test

import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig

import com.ss.jcrm.registration.web.config.RegistrationWebConfig
import crm.integration.test.db.config.DbTestConfig
import crm.mail.config.MailTestConfig
import crm.user.jasync.config.JAsyncUserTestConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    RegistrationWebConfig,
    DbTestConfig,
    JAsyncDictionaryTestConfig,
    JAsyncUserTestConfig,
    MailTestConfig
])
@PropertySource("classpath:com/ss/jcrm/registration/web/test/registration-web-test.properties")
class RegistrationSpecificationConfig {
}
