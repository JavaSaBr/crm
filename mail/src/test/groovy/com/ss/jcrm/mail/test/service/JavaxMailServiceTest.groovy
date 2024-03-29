package com.ss.jcrm.mail.test.service

import crm.mail.service.MailService
import crm.mail.MailSpecification
import org.springframework.beans.factory.annotation.Autowired

class JavaxMailServiceTest extends MailSpecification {
    
    @Autowired
    MailService mailService
    
    def "should send an email successfully"() {
        
        given:
            def subject = "Integration test of Javax Mail Service"
            def content = "Integration test of Javax Mail Service"
        when:
            mailService.send("email@email.com", subject, content).block()
        then:
            noExceptionThrown()
        when:
            def count = smtpTestContainer.getEmailCountFrom('test@test.test')
        then:
            count == 1
        when:
            mailService.send("email@email.com", subject, content).block()
            count = smtpTestContainer.getEmailCountFrom('test@test.test')
        then:
            count == 2
    }
}
