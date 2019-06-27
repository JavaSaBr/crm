package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.test.RegistrationSpecification

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE

class UserHandlerTest extends RegistrationSpecification {

    def "should found that an user is exist"() {

        given:
            def user = userTestHelper.newUser()
        when:
            def response = client.get()
                .url("/registration/exist/user/email/$user.email")
                .exchange()
        then:
            response.expectStatus().isOk()
    }

    def "should found that an user is not exist"() {
    
        given:
            def email = "nonexist@test.com"
        when:
            def response = client.get()
                .url("/registration/exist/user/email/$email")
                .exchange()
        then:
            response.expectStatus().isNotFound()
    }
    
    def "should bad request if email invalid"() {
    
        given:
            def email = "@&%&#2"
        when:
            def response = client.get()
                .url("/registration/exist/user/email/$email")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
    }
}
