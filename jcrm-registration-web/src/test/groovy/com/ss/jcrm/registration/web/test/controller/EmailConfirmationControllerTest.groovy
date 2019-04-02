package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.test.RegistrationSpecification

class EmailConfirmationControllerTest extends RegistrationSpecification {

    def "should send email confirmation request successfully"() {

        given:
            def email = "testemail@test.com"
        when:
            def response = client.get()
                .url("/registration/email/confirmation/" + email)
                .exchange()
        then:
            response.expectStatus()
                .isOk()
    }

    def "should not send email confirmation with invalid email"() {

        given:
            def email = "@&%&#2"
        when:
            def response = client.get()
                .url("/registration/email/confirmation/" + email)
                .exchange()
        then:
            response.expectStatus()
                .isOk()
    }
}
