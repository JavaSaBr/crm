package com.ss.jcrm.registration.web.test.controller


import com.ss.jcrm.registration.web.test.RegistrationSpecification

class AuthenticationControllerTest extends RegistrationSpecification {
/*
    def "should authenticate a user"() {

        given:

            def user = userTestHelper.newTestUser("User1")
            def data = JsonStream.serialize(new AuthenticationRequest(user.name, user.password))

        when:

            def response = mvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(data))
                .andReturn()

        then:
            mvc.perform(asyncDispatch(response))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.token', notNullValue()))
                .andReturn()
    }*/
}
