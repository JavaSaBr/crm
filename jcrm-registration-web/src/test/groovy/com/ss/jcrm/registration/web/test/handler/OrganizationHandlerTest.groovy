package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.user.api.dao.EmailConfirmationDao
import com.ss.jcrm.user.contact.api.PhoneNumberType
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource
import org.springframework.beans.factory.annotation.Autowired

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.COUNTRY_NOT_FOUND
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.COUNTRY_NOT_FOUND_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACTIVATION_CODE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACTIVATION_CODE_MESSAGE
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE
import static org.hamcrest.Matchers.is;

class OrganizationHandlerTest extends RegistrationSpecification {
    
    @Autowired
    EmailConfirmationDao emailConfirmationDao
    
    def "should found that an organization is exist"() {
        
        given:
            def organization = userTestHelper.newOrg()
        when:
            def response = client.get()
                .url("/registration/exist/organization/name/$organization.name")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
    
    def "should found that an organization is not exist"() {
        
        when:
            def response = client.get()
                .url("/registration/exist/organization/name/nonexist")
                .exchange()
        then:
            response.expectStatus().isNotFound()
    }
    
    def "should register a new organization"() {
        
        given:
            def country = dictionaryTestHelper.newCountry()
            def confirmation = userTestHelper.newEmailConfirmation()
            def request = new OrganizationRegisterInResource(
                "test_org",
                confirmation.email,
                confirmation.code,
                '123456'.toCharArray(),
                new PhoneNumberResource("+7", "23", "2311312", null),
                country.id
            )
        when:
            def response = client.post()
                .body(request)
                .url("/registration/register/organization")
                .exchange()
        then:
            response.expectStatus().isCreated()
                .expectBody()
                    .jsonPath('$.token').isNotEmpty()
                    .jsonPath('$.user').isNotEmpty()
                    .jsonPath('$.user.id').isNotEmpty()
                    .jsonPath('$.user.email').value(is(confirmation.email))
                    .jsonPath('$.user.phoneNumber.countryCode').value(is(request.phoneNumber.countryCode))
                    .jsonPath('$.user.phoneNumber.regionCode').value(is(request.phoneNumber.regionCode))
                    .jsonPath('$.user.phoneNumber.phoneNumber').value(is(request.phoneNumber.phoneNumber))
                    .jsonPath('$.user.phoneNumber.type').value(is(PhoneNumberType.UNKNOWN.name()))
    }
    
    def "should not register an organization with wrong activation code"() {
        
        given:
            def country = dictionaryTestHelper.newCountry()
            def confirmation = userTestHelper.newEmailConfirmation()
            def request = new OrganizationRegisterInResource(
                "test_org",
                confirmation.email,
                confirmation.code + "111",
                '123456'.toCharArray(),
                new PhoneNumberResource("+7", "23", "2311312", null),
                country.id
            )
        when:
            def response = client.post()
                .body(request)
                .url("/registration/register/organization")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_ACTIVATION_CODE, INVALID_ACTIVATION_CODE_MESSAGE)
    }
    
    def "should not register an organization with wrong country"() {
        
        given:
            def country = dictionaryTestHelper.newCountry()
            def confirmation = userTestHelper.newEmailConfirmation()
            def request = new OrganizationRegisterInResource(
                "test_org",
                confirmation.email,
                confirmation.code,
                '123456'.toCharArray(),
                new PhoneNumberResource("+7", "23", "2311312", null),
                country.id + 100000
            )
        when:
            def response = client.post()
                .body(request)
                .url("/registration/register/organization")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(COUNTRY_NOT_FOUND, COUNTRY_NOT_FOUND_MESSAGE)
    }
    
    def "should not register an organization with wrong email"() {
        
        given:
            def country = dictionaryTestHelper.newCountry()
            def confirmation = userTestHelper.newEmailConfirmation()
            def request = new OrganizationRegisterInResource(
                "test_org",
                "wrongemail11",
                confirmation.code,
                '123456'.toCharArray(),
                new PhoneNumberResource("+7", "23", "2311312", null),
                country.id
            )
        when:
            def response = client.post()
                .body(request)
                .url("/registration/register/organization")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
    }
}
