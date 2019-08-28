package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.resources.AuthenticationInResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.security.web.exception.SecurityErrors
import com.ss.jcrm.security.web.service.UnsafeTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import java.time.ZonedDateTime

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.EMPTY_LOGIN
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.EMPTY_LOGIN_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.EXPIRED_TOKEN
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.EXPIRED_TOKEN_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_TOKEN
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_TOKEN_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.MAX_REFRESHED_TOKEN
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.MAX_REFRESHED_TOKEN_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.EXPIRED_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.EXPIRED_TOKEN_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.INVALID_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.INVALID_TOKEN_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.MAX_REFRESHED_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.MAX_REFRESHED_TOKEN_MESSAGE
import static org.hamcrest.Matchers.is

class AuthenticationHandlerTest extends RegistrationSpecification {
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
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
    
    def "should authenticate a user by a token"() {
        
        given:
            def user = userTestHelper.newUser()
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .url("/registration/authenticate/$token")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                .jsonPath('$.token').isNotEmpty()
                .jsonPath('$.user').isNotEmpty()
                .jsonPath('$.user.id').isNotEmpty()
    }
    
    def "should refresh an expired token"() {
        
        given:
            def user = userTestHelper.newUser()
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now().minusDays(2),
                0,
                0
            )
        when:
            def response = client.get()
                .url("/registration/token/refresh/$token")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$.token').isNotEmpty()
                    .jsonPath('$.user').isNotEmpty()
                    .jsonPath('$.user.id').isNotEmpty()
    }
    
    def "should not refresh an invalid token"() {
        
        given:
            def user = userTestHelper.newUser()
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(1),
                0,
                0
            )
        when:
            def response = client.get()
                .url("/registration/token/refresh/$token")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
        when:
            token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                1000_000,
                0
            )
            response = client.get()
                .url("/registration/token/refresh/$token")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(MAX_REFRESHED_TOKEN, MAX_REFRESHED_TOKEN_MESSAGE)
        when:
            token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                0,
                100
            )
            response = client.get()
                .url("/registration/token/refresh/$token")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
        when:
            response = client.get()
                .url("/registration/token/refresh/invalidtoken")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
    }
    
    def "should not authenticate a user by an invalid token"() {
        
        given:
            def user = userTestHelper.newUser()
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                0,
                0
            )
        when:
            def response = client.get()
                .url("/registration/authenticate/$token")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(EXPIRED_TOKEN, EXPIRED_TOKEN_MESSAGE)
        when:
            token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().plusDays(6),
                ZonedDateTime.now().plusDays(5),
                0,
                0
            )
            response = client.get()
                .url("/registration/authenticate/$token")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
        when:
            response = client.get()
                .url("/registration/authenticate/wefewfewfewf")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
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
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
        when:
            response = client.post()
                .body(new AuthenticationInResource(phoneNumber, "invalidpwd".toCharArray()))
                .url("/registration/authenticate")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
        when:
            response = client.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body("{}")
                .url("/registration/authenticate")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(EMPTY_LOGIN, EMPTY_LOGIN_MESSAGE)
    }
}
