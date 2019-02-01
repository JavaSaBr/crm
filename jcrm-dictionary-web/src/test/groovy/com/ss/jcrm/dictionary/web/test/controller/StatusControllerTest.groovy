package com.ss.jcrm.dictionary.web.test.controller

import com.ss.jcrm.dictionary.web.test.DictionarySpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class StatusControllerTest extends DictionarySpecification {

    def "should return status ok"() {

        when:
            def response = mvc.perform(get("/status"))
        then:
            response.andExpect(status().isOk())
    }
}
