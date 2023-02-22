package com.ss.jcrm.security.web.test

import crm.integration.test.web.WebSpecification
import com.ss.jcrm.security.web.service.UnsafeTokenService
import crm.user.api.UserDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = WebSecuritySpecificationConfig)
class WebSecuritySpecification extends WebSpecification {

    @Autowired
    UserDbTestHelper userTestHelper
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    def setup() {
    }
}
