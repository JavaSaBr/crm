package com.ss.jcrm.mail.test

import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.rlib.testcontainers.FakeSMTPTestContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = MailSpecificationConfig)
class MailSpecification extends DefaultSpecification {
    
    @Autowired
    FakeSMTPTestContainer smtpTestContainer
    
    def setup() {
        smtpTestContainer.deleteEmails()
    }
}
