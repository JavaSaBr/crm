package com.ss.jcrm.dictionary.web.test

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
