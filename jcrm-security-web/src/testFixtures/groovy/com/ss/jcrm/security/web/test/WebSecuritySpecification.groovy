package com.ss.jcrm.security.web.test

import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.security.web.service.UnsafeTokenService
import crm.user.api.test.UserTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = WebSecuritySpecificationConfig)
class WebSecuritySpecification extends WebSpecification {

    @Autowired
    UserTestHelper userTestHelper
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    def setup() {
        userTestHelper.clearAllData()
    }
}
