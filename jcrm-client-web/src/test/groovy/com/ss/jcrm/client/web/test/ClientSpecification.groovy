package com.ss.jcrm.client.web.test

import com.ss.jcrm.client.api.test.ClientTestHelper
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.user.api.test.UserTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = ClientSpecificationConfig)
class ClientSpecification extends WebSpecification {

    @Autowired
    UserTestHelper userTestHelper

    @Autowired
    DictionaryTestHelper dictionaryTestHelper
    
    @Autowired
    ClientTestHelper clientTestHelper
    
    def setup() {
        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()
        clientTestHelper.clearAllData()
    }
}
