package com.ss.jcrm.dictionary.web.test

import crm.base.util.Reloadable
import crm.integration.test.web.WebSpecification
import crm.base.web.config.ApiEndpoint
import crm.dictionary.api.DictionaryDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = DictionarySpecificationConfig)
class DictionarySpecification extends WebSpecification {

    @Autowired
    ApiEndpoint dictionaryApiEndpointServer

    @Autowired
    DictionaryDbTestHelper dictionaryTestHelper

    @Autowired
    List<Reloadable> reloadableServices

    def contextPath

    def setup() {
      
        reloadableServices.each {
            it.reload()
        }

        contextPath = dictionaryApiEndpointServer.contextPath()
    }
}
