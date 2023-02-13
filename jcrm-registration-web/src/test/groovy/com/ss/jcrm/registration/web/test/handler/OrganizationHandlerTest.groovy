package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import crm.contact.api.PhoneNumberType
import crm.contact.api.resource.PhoneNumberResource
import crm.user.api.dao.EmailConfirmationDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.COUNTRY_NOT_FOUND
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.COUNTRY_NOT_FOUND_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACTIVATION_CODE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACTIVATION_CODE_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class OrganizationHandlerTest extends RegistrationSpecification {
  
  @Autowired
  EmailConfirmationDao emailConfirmationDao
  
  def "should found that organization is exist"() {
    
    given:
        def organization = userTestHelper.newOrganization()
    when:
        def response = webClient.get()
            .url("$contextPath/exist/organization/name/$organization.name")
            .exchange()
    then:
        response.expectStatus().isOk()
  }
  
  def "should found that organization is not exist"() {
    
    when:
        def response = webClient.get()
            .url("$contextPath/exist/organization/name/nonexist")
            .exchange()
    then:
        response.expectStatus().isNotFound()
  }
  
  def "should register new organization"() {
    
    given:
        def country = dictionaryTestHelper.newCountry()
        def confirmation = userTestHelper.newEmailConfirmation()
        def request = OrganizationRegisterInResource.from(
            "test_org",
            confirmation.email,
            confirmation.code,
            '123456'.toCharArray(),
            new PhoneNumberResource("+7", "23", "2311312", 0),
            country.id
        )
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .url("$contextPath/register/organization")
            .exchange()
    then:
        response
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath('$.token').isNotEmpty()
            .jsonPath('$.user').isNotEmpty()
            .jsonPath('$.user.id').isNotEmpty()
            .jsonPath('$.user.email').value(is(confirmation.email))
            .jsonPath('$.user.phoneNumbers').value(hasSize(1))
            .jsonPath('$.user.phoneNumbers[0].countryCode').isEqualTo(request.phoneNumber().countryCode())
            .jsonPath('$.user.phoneNumbers[0].regionCode').isEqualTo(request.phoneNumber().regionCode())
            .jsonPath('$.user.phoneNumbers[0].phoneNumber').isEqualTo(request.phoneNumber().phoneNumber())
            .jsonPath('$.user.phoneNumbers[0].type').isEqualTo((int) PhoneNumberType.UNKNOWN.id())
  }
  
  def "should not register organization with wrong activation code"() {
    
    given:
        def country = dictionaryTestHelper.newCountry()
        def confirmation = userTestHelper.newEmailConfirmation()
        def request = OrganizationRegisterInResource.from(
            "test_org",
            confirmation.email,
            confirmation.code + "111",
            '123456'.toCharArray(),
            new PhoneNumberResource("+7", "23", "2311312", 0),
            country.id
        )
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .url("$contextPath/register/organization")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_ACTIVATION_CODE, INVALID_ACTIVATION_CODE_MESSAGE)
  }
  
  def "should not register organization with wrong country"() {
    
    given:
        def country = dictionaryTestHelper.newCountry()
        def confirmation = userTestHelper.newEmailConfirmation()
        def request = OrganizationRegisterInResource.from(
            "test_org",
            confirmation.email,
            confirmation.code,
            '123456'.toCharArray(),
            new PhoneNumberResource("+7", "23", "2311312", 0),
            country.id + 100000
        )
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .url("$contextPath/register/organization")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(COUNTRY_NOT_FOUND, COUNTRY_NOT_FOUND_MESSAGE)
  }
  
  def "should not register organization with wrong email"() {
    
    given:
        def country = dictionaryTestHelper.newCountry()
        def confirmation = userTestHelper.newEmailConfirmation()
        def request = OrganizationRegisterInResource.from(
            "test_org",
            "wrongemail11",
            confirmation.code,
            '123456'.toCharArray(),
            new PhoneNumberResource("+7", "23", "2311312", 0),
            country.id
        )
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .url("$contextPath/register/organization")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
  }
}
