package com.ss.jcrm.dictionary.web.test.controller

import com.ss.jcrm.dictionary.web.test.DictionarySpecification
import org.springframework.test.web.reactive.server.WebTestClient

class StatusControllerTest extends DictionarySpecification {

    def "should return status ok"() {

        when:

            WebTestClient.ResponseSpec response = client.get()
                .uri("/dictionary/status")
                .exchange()

        then:
            response.expectStatus().isOk()
    }
}
