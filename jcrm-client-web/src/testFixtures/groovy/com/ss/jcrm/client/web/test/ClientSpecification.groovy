package com.ss.jcrm.client.web.test

import com.ss.jcrm.client.api.test.ClientDbTestHelper
import com.ss.jcrm.dictionary.api.test.DictionaryDbTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification

import com.ss.jcrm.web.config.ApiEndpointServer
import crm.user.api.UserDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = ClientSpecificationConfig)
class ClientSpecification extends WebSpecification {

    @Autowired
    ApiEndpointServer clientApiEndpointServer

    @Autowired
    UserDbTestHelper userTestHelper

    @Autowired
    DictionaryDbTestHelper dictionaryTestHelper
    
    @Autowired
    ClientDbTestHelper clientTestHelper

    def contextPath

    def setup() {
        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()
        clientTestHelper.clearAllData()

        contextPath = clientApiEndpointServer.contextPath()
    }
}
