package com.ss.jcrm.registration.web.test.controller

import com.jsoniter.output.JsonStream
import com.ss.jcrm.registration.web.resources.UserRegisterResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.security.Passwords
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RegistrationControllerTest extends RegistrationSpecification {

    def "should create a new user"() {

        given:

            def data = JsonStream.serialize(
                new UserRegisterResource("User1", Passwords.nextPassword(24))
            )

        when:

            def mvcResult = mvc.perform(post("/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(data)
            )

        then:
            mvcResult.andExpect(status().is2xxSuccessful())
    }
}
