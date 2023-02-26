package crm.security

import crm.integration.test.DefaultSpecification
import crm.security.config.SecurityTestConfig
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = SecurityTestConfig)
class SecuritySpecification extends DefaultSpecification {
}
