package com.ss.jcrm.registration.web.test

import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig
import integration.test.db.config.DbTestConfig
import com.ss.jcrm.mail.test.MailSpecificationConfig
import com.ss.jcrm.registration.web.config.RegistrationWebConfig
import crm.user.jasync.config.JAsyncUserTestConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    RegistrationWebConfig,
    DbTestConfig,
    JAsyncDictionarySpecificationConfig,
    JAsyncUserTestConfig,
    MailSpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/registration/web/test/registration-web-test.properties")
class RegistrationSpecificationConfig {
}
