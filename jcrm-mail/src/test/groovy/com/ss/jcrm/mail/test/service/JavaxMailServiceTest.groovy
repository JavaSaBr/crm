package com.ss.jcrm.mail.test.service

import com.ss.jcrm.mail.service.MailService
import com.ss.jcrm.mail.test.MailSpecification
import org.springframework.beans.factory.annotation.Autowired

class JavaxMailServiceTest extends MailSpecification {
    
    @Autowired
    MailService mailService
    
    def "should send an email successfully"() {
        
        given:
            def subject = "Integration test of Javax Mail Service"
            def content = "Integration test of Javax Mail Service"
        when:
            mailService.send("email@email.com", subject, content)
        then:
            noExceptionThrown()
        when:
            def count = smtpTestContainer.getEmailCountFrom('test@test.test')
        then:
            count == 1
        when:
            mailService.send("email@email.com", subject, content)
            count = smtpTestContainer.getEmailCountFrom('test@test.test')
        then:
            count == 2
    }
}
