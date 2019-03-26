package com.ss.jcrm.registration.web.test

import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.user.api.test.UserTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = RegistrationSpecificationConfig)
class RegistrationSpecification extends WebSpecification {

    @Autowired
    UserTestHelper userTestHelper

    @Autowired
    DictionaryTestHelper dictionaryTestHelper

    def setup() {
        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()
    }
}
