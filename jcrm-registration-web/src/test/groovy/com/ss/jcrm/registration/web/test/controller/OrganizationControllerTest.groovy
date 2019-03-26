package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.test.RegistrationSpecification

class OrganizationControllerTest extends RegistrationSpecification {

    def "should found that an organization is exist"() {

        given:
            def organization = userTestHelper.newOrg()
        when:
            def response = client.get()
                .url("/registration/organization/exist/name/" + organization.name)
                .exchange()
        then:
            response.expectStatus()
                .isOk()
    }

    def "should found that an organization is not exist"() {

        when:
            def response = client.get()
                .url("/registration/organization/exist/name/" + "nonexist")
                .exchange()
        then:
            response.expectStatus()
                .isNotFound()
    }
}
