package com.ss.jcrm.registration.web.test

import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.mail.test.MailSpecificationConfig
import com.ss.jcrm.registration.web.config.RegistrationWebConfig
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecificationConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    RegistrationWebConfig,
    DbSpecificationConfig,
    JAsyncDictionarySpecificationConfig,
    JAsyncUserSpecificationConfig,
    MailSpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/registration/web/test/registration-web-test.properties")
class RegistrationSpecificationConfig {
}
