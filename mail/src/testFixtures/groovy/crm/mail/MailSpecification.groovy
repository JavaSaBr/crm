package crm.mail

import crm.integration.test.DefaultSpecification
import com.ss.rlib.testcontainers.FakeSMTPTestContainer
import crm.mail.config.MailTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = MailTestConfig)
class MailSpecification extends DefaultSpecification {
  
  @Autowired
  FakeSMTPTestContainer smtpTestContainer
  
  def setup() {
    smtpTestContainer.deleteEmails()
  }
}
