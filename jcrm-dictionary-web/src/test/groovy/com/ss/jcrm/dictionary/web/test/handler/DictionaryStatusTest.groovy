package com.ss.jcrm.dictionary.web.test.handler

import com.ss.jcrm.dictionary.web.test.DictionarySpecification

class DictionaryStatusTest extends DictionarySpecification {

    def "should return status ok"() {

        when:
            def response = client.get()
                .url("/dictionary/status")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
}
