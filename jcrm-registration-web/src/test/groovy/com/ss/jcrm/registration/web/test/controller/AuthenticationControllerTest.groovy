package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.resources.AuthenticationInResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.EMPTY_LOGIN
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.EMPTY_LOGIN_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS_MESSAGE
import static org.hamcrest.Matchers.is

class AuthenticationControllerTest extends RegistrationSpecification {

    def "should authenticate a user by email"() {
    
        given:
            def email = "test@test.com"
            def password = "pwdpwd"
            userTestHelper.newUser(email, null, password)
        when:
    
            def response = client.post()
                .body(new AuthenticationInResource(email, password.toCharArray()))
                .url("/registration/authenticate")
                .exchange()

        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$.token').isNotEmpty()
                    .jsonPath('$.user').isNotEmpty()
                    .jsonPath('$.user.id').isNotEmpty()
                    .jsonPath('$.user.email').value(is(email))
    }
    
    def "should authenticate a user by phone number"() {
        
        given:
            def email = "test@test.com"
            def password = "pwdpwd"
            def phoneNumber = "+37533123123"
            userTestHelper.newUser(email, phoneNumber, password)
        when:
            
            def response = client.post()
                .body(new AuthenticationInResource(phoneNumber, password.toCharArray()))
                .url("/registration/authenticate")
                .exchange()
        
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$.token').isNotEmpty()
                    .jsonPath('$.user').isNotEmpty()
                    .jsonPath('$.user.id').isNotEmpty()
                    .jsonPath('$.user.email').value(is(email))
                    .jsonPath('$.user.phoneNumber').value(is(phoneNumber))
    }
    
    def "should not authenticate a user with invalid credentials"() {
        
        given:
            def email = "test@test.com"
            def password = "pwdpwd"
            def phoneNumber = "+37533123123"
            userTestHelper.newUser(email, phoneNumber, password)
        when:
            
            def response = client.post()
                .body(new AuthenticationInResource("+3751231212", password.toCharArray()))
                .url("/registration/authenticate")
                .exchange()
        
        then:
            response.expectStatus().isBadRequest()
                .verifyBadRequest(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
        when:
        
            response = client.post()
                .body(new AuthenticationInResource(phoneNumber, "invalidpwd".toCharArray()))
                .url("/registration/authenticate")
                .exchange()
    
        then:
            response.expectStatus().isBadRequest()
                .verifyBadRequest(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
        when:
        
            response = client.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body("{}")
                .url("/registration/authenticate")
                .exchange()
    
        then:
            response.expectStatus().isBadRequest()
                .verifyBadRequest(EMPTY_LOGIN, EMPTY_LOGIN_MESSAGE)
    }
}
