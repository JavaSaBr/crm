package com.ss.jcrm.registration.web.test

import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.user.jdbc.test.helper.UserTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@WebAppConfiguration
@ContextConfiguration(classes = RegistrationSpecificationConfig)
class RegistrationSpecification extends WebSpecification {

    @Autowired
    UserTestHelper userTestHelper

    def setup() {
        userTestHelper.clearAllData()
    }
}
