package com.ss.jcrm.dictionary.web.test

import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = DictionarySpecificationConfig)
class DictionarySpecification extends WebSpecification {

    @Autowired
    DictionaryTestHelper dictionaryTestHelper

    def setup() {
        dictionaryTestHelper.clearAllData()
    }
}
