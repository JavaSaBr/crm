package crm.client.web

import com.ss.jcrm.client.web.test.ClientSpecification

class ClientStatusTest extends ClientSpecification {

    def "should return status ok"() {

        when:
            def response = webClient.get()
                .url("$contextPath/status")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
}
