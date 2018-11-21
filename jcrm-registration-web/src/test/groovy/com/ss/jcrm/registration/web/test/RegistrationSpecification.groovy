package com.ss.jcrm.registration.web.test

import com.ss.jcrm.integration.test.web.WebSpecification
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@WebAppConfiguration
@ContextConfiguration(classes = RegistrationConfigTest)
class RegistrationSpecification extends WebSpecification {
}
