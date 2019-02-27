package com.ss.jcrm.dictionary.web.test.controller

import com.ss.jcrm.dictionary.web.test.DictionarySpecification

class StatusControllerTest extends DictionarySpecification {

    def "should return status ok"() {

        when:

            def response = client.get()
                .url("/dictionary/status")
                .exchange()

        then:

            response.expectStatus().isOk()
    }
}
