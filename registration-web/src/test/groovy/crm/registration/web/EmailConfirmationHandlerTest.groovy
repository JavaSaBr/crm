package crm.registration.web

import java.nio.charset.StandardCharsets

import static crm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static crm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE

class EmailConfirmationHandlerTest extends RegistrationSpecification {

    def "should send email confirmation request successfully"() {

        given:
            def email = "testemail@test.com"
        when:
            def response = webClient.get()
                .url("$contextPath/email-confirmation/$email")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
    
    def "should not send email confirmation with invalid email"() {
        
        given:
            def email = URLEncoder.encode("@&%&#2", StandardCharsets.UTF_8)
        when:
            def response = webClient.get()
                .url("$contextPath/email-confirmation/$email")
                .exchange()
        then:
            response
                .expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
    }
}
