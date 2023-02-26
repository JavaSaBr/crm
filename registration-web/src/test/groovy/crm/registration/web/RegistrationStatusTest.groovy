package crm.registration.web

import crm.registration.web.RegistrationSpecification

class RegistrationStatusTest extends RegistrationSpecification {

    def "should return status ok"() {

        when:
            def response = webClient.get()
                .url("$contextPath/status")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
}
