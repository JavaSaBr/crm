package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.test.RegistrationSpecification
import org.springframework.test.web.reactive.server.WebTestClient

class StatusControllerTest extends RegistrationSpecification {

    def "should return status ok"() {

        when:

            WebTestClient.ResponseSpec response = client.get()
                .uri("/registration/status")
                .exchange()

        then:
            response.expectStatus().isOk()
    }
}
