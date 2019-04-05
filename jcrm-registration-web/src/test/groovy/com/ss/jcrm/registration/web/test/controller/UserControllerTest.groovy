package com.ss.jcrm.registration.web.test.controller

import com.ss.jcrm.registration.web.test.RegistrationSpecification

class UserControllerTest extends RegistrationSpecification {

    def "should found that an user is exist"() {

        given:
            def user = userTestHelper.newUser()
        when:
            def response = client.get()
                .url("/registration/exist/user/name/$user.name")
                .exchange()
        then:
            response.expectStatus().isOk()
    }

    def "should found that an user is not exist"() {

        when:
            def response = client.get()
                .url("/registration/exist/user/name/nonexist@test.com")
                .exchange()
        then:
            response.expectStatus().isNotFound()
    }
}
