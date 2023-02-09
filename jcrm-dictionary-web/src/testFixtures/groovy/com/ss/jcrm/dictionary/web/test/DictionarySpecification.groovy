package com.ss.jcrm.dictionary.web.test

import com.ss.jcrm.base.utils.Reloadable
import com.ss.jcrm.dictionary.api.test.DictionaryDbTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.web.config.ApiEndpointServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = DictionarySpecificationConfig)
class DictionarySpecification extends WebSpecification {

    @Autowired
    ApiEndpointServer dictionaryApiEndpointServer

    @Autowired
    DictionaryDbTestHelper dictionaryTestHelper

    @Autowired
    List<Reloadable> reloadableServices

    def contextPath

    def setup() {

        dictionaryTestHelper.clearAllData()

        reloadableServices.each {
            it.reload()
        }

        contextPath = dictionaryApiEndpointServer.contextPath()
    }
}
