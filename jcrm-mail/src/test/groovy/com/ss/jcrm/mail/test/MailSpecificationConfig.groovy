package com.ss.jcrm.mail.test

import com.ss.jcrm.mail.MailConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import(MailConfig)
@Configuration
@PropertySource("classpath:com/ss/jcrm/mail/test/mail-test.properties")
class MailSpecificationConfig {


}