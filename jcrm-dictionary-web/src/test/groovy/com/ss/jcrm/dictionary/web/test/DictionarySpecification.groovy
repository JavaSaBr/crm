package com.ss.jcrm.dictionary.web.test

import com.ss.jcrm.dictionary.jdbc.test.helper.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@WebAppConfiguration
@ContextConfiguration(classes = DictionarySpecificationConfig)
class DictionarySpecification extends WebSpecification {

    @Autowired
    DictionaryTestHelper dictionaryTestHelper

    def setup() {
        dictionaryTestHelper.clearAllData()
    }
}
