package com.ss.jcrm.client.web.test

import com.ss.jcrm.client.api.test.ClientTestHelper
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import crm.user.api.test.UserTestHelper
import com.ss.jcrm.web.config.ApiEndpointServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = ClientSpecificationConfig)
class ClientSpecification extends WebSpecification {

    @Autowired
    ApiEndpointServer clientApiEndpointServer

    @Autowired
    UserTestHelper userTestHelper

    @Autowired
    DictionaryTestHelper dictionaryTestHelper
    
    @Autowired
    ClientTestHelper clientTestHelper

    def contextPath

    def setup() {
        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()
        clientTestHelper.clearAllData()

        contextPath = clientApiEndpointServer.contextPath()
    }
}
