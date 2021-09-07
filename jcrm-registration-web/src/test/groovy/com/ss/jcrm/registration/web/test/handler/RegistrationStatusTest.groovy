package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.test.RegistrationSpecification

class RegistrationStatusTest extends RegistrationSpecification {

    def "should return status ok"() {

        when:
            def response = client.get()
                .url("/registration/status")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
}
