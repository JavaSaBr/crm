package com.ss.jcrm.client.web.test

import crm.integration.test.web.WebSpecification

import crm.base.web.config.ApiEndpoint
import crm.client.api.ClientDbTestHelper
import crm.dictionary.api.DictionaryDbTestHelper
import crm.user.api.UserDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = ClientSpecificationConfig)
class ClientSpecification extends WebSpecification {

    @Autowired
    ApiEndpoint clientApiEndpointServer

    @Autowired
    UserDbTestHelper userTestHelper

    @Autowired
    DictionaryDbTestHelper dictionaryTestHelper
    
    @Autowired
    ClientDbTestHelper clientTestHelper

    def contextPath

    def setup() {
        contextPath = clientApiEndpointServer.contextPath()
    }
}
