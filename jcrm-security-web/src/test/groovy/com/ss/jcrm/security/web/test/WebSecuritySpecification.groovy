package com.ss.jcrm.security.web.test

import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.user.api.test.UserTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = WebSecuritySpecificationConfig)
class WebSecuritySpecification extends DefaultSpecification {

    @Autowired
    UserTestHelper userTestHelper

    def setup() {
        userTestHelper.clearAllData()
    }
}
