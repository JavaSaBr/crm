package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.resources.OrganizationRegisterInResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.user.api.dao.EmailConfirmationDao
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired

import static org.hamcrest.Matchers.is;

class OrganizationControllerTest extends RegistrationSpecification {
    
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
            def request = new OrganizationRegisterInResource()
            request.setOrgName("test_org")
            request.activationCode = confirmation.code
            request.email = confirmation.email
            request.countryId = country.id
            request.password = '123456'.toCharArray()
            request.phoneNumber = '+1234567'
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
                .jsonPath('$.user.name').value(is(confirmation.email))
                .jsonPath('$.user.phoneNumber').value(is(request.phoneNumber))
    }
}
