package crm.registration.web

import crm.registration.web.resources.AuthenticationInResource
import crm.security.web.service.UnsafeTokenService
import crm.contact.api.PhoneNumber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import java.time.ZonedDateTime

import static crm.registration.web.exception.RegistrationErrors.EMPTY_LOGIN
import static crm.registration.web.exception.RegistrationErrors.EMPTY_LOGIN_MESSAGE
import static crm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS
import static crm.registration.web.exception.RegistrationErrors.INVALID_CREDENTIALS_MESSAGE
import static crm.security.web.exception.SecurityErrors.EXPIRED_TOKEN
import static crm.security.web.exception.SecurityErrors.EXPIRED_TOKEN_MESSAGE
import static crm.security.web.exception.SecurityErrors.INVALID_TOKEN
import static crm.security.web.exception.SecurityErrors.INVALID_TOKEN_MESSAGE
import static crm.security.web.exception.SecurityErrors.MAX_REFRESHED_TOKEN
import static crm.security.web.exception.SecurityErrors.MAX_REFRESHED_TOKEN_MESSAGE
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class AuthenticationHandlerTest extends RegistrationSpecification {
  
  @Autowired
  UnsafeTokenService unsafeTokenService
  
  def "should authenticate a user by email"() {
    given:
        def email = userTestHelper.nextEmail();
        def password = "pwdpwd"
        userTestHelper.newUser(email, password)
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new AuthenticationInResource(email, password.toCharArray()))
            .url("$contextPath/authenticate")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$.token').isNotEmpty()
            .jsonPath('$.user').isNotEmpty()
            .jsonPath('$.user.id').isNotEmpty()
            .jsonPath('$.user.email').value(is(email))
  }
  
  def "should authenticate a user by phone number"() {
    given:
        def email = userTestHelper.nextEmail()
        def password = "pwdpwd"
        def phoneNumber = PhoneNumber.of("+375", "33", "123123")
        
        userTestHelper.newUser(
            email,
            Set.of(phoneNumber),
            Set.of(),
            password)
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new AuthenticationInResource(phoneNumber.fullPhoneNumber(), password.toCharArray()))
            .url("$contextPath/authenticate")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$.token').isNotEmpty()
            .jsonPath('$.user').isNotEmpty()
            .jsonPath('$.user.id').isNotEmpty()
            .jsonPath('$.user.email').isEqualTo(email)
            .jsonPath('$.user.phoneNumbers').value(hasSize(1))
            .jsonPath('$.user.phoneNumbers[0].countryCode').isEqualTo(phoneNumber.countryCode())
            .jsonPath('$.user.phoneNumbers[0].regionCode').isEqualTo(phoneNumber.regionCode())
            .jsonPath('$.user.phoneNumbers[0].phoneNumber').isEqualTo(phoneNumber.phoneNumber())
  }
  
  def "should authenticate a user by a token"() {
    
    given:
        def user = userTestHelper.newUser()
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .url("$contextPath/authenticate/$token")
            .exchange()
    then:
        response
            .expectStatus().isOk()
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
        def response = webClient.get()
            .url("$contextPath/token/refresh/$token")
            .exchange()
    then:
        response
            .expectStatus().isOk()
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
        def response = webClient.get()
            .url("$contextPath/token/refresh/$token")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
    when:
        token = unsafeTokenService.generateNewToken(
            user.id,
            ZonedDateTime.now().minusDays(300),
            ZonedDateTime.now().minusDays(350),
            1000_000,
            0
        )
        response = webClient.get()
            .url("$contextPath/token/refresh/$token")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(MAX_REFRESHED_TOKEN, MAX_REFRESHED_TOKEN_MESSAGE)
    when:
        token = unsafeTokenService.generateNewToken(
            user.id,
            ZonedDateTime.now().minusDays(300),
            ZonedDateTime.now().minusDays(350),
            0,
            100
        )
        response = webClient.get()
            .url("$contextPath/token/refresh/$token")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
    when:
        response = webClient.get()
            .url("$contextPath/token/refresh/invalidtoken")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
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
        def response = webClient.get()
            .url("$contextPath/authenticate/$token")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(EXPIRED_TOKEN, EXPIRED_TOKEN_MESSAGE)
    when:
        token = unsafeTokenService.generateNewToken(
            user.id,
            ZonedDateTime.now().plusDays(6),
            ZonedDateTime.now().plusDays(5),
            0,
            0
        )
        response = webClient.get()
            .url("$contextPath/authenticate/$token")
            .exchange()
    then:
        response.expectStatus().isUnauthorized()
            .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
    when:
        response = webClient.get()
            .url("$contextPath/authenticate/wefewfewfewf")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(INVALID_TOKEN, INVALID_TOKEN_MESSAGE)
  }
  
  def "should not authenticate a user with invalid credentials"() {
    given:
        def email = userTestHelper.nextEmail()
        def password = "pwdpwd"
        def phoneNumber = PhoneNumber.of("+375", "33", "123123")
        userTestHelper.newUser(email, Set.of(phoneNumber), Set.of(), password)
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new AuthenticationInResource("+3751231212", password.toCharArray()))
            .url("$contextPath/authenticate")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
    when:
        response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new AuthenticationInResource(phoneNumber.fullPhoneNumber(), "invalidpwd".toCharArray()))
            .url("$contextPath/authenticate")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
    when:
        response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{}")
            .url("$contextPath/authenticate")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(EMPTY_LOGIN, EMPTY_LOGIN_MESSAGE)
  }
}
