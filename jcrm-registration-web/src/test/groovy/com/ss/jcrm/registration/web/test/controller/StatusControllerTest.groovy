package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.test.RegistrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class StatusControllerTest extends RegistrationSpecification {

    def "should return status ok"() {

        when:
            def response = mvc.perform(get("/status"))

        then:
            response.andExpect(status().isOk())
    }
}
