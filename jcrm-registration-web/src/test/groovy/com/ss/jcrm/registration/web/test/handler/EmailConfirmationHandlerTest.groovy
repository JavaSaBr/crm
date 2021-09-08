package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.test.RegistrationSpecification

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE

class EmailConfirmationHandlerTest extends RegistrationSpecification {

    def "should send email confirmation request successfully"() {

        given:
            def email = "testemail@test.com"
        when:
            def response = webClient.get()
                .url("/registration/email-confirmation/$email")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
    
    def "should not send email confirmation with invalid email"() {
        
        given:
            def email = "@&%&#2"
        when:
            def response = webClient.get()
                .url("/registration/email-confirmation/$email")
                .exchange()
        then:
            response
                .expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
    }
}
