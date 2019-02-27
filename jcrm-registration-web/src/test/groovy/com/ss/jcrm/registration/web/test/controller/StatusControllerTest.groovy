package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.test.RegistrationSpecification

class StatusControllerTest extends RegistrationSpecification {

    def "should return status ok"() {

        when:
            def response = client.get()
                .url("/registration/status")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
}
