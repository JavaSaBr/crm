package com.ss.jcrm.registration.web.test.controller

import com.jsoniter.output.JsonStream
import com.ss.jcrm.registration.web.resources.UserRegisterResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification

import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserRoleDao
import com.ss.rlib.common.util.array.ArrayFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RegistrationControllerTest extends RegistrationSpecification {

    @Autowired
    OrganizationDao organizationDao

    @Autowired
    UserRoleDao userRoleDao

  /*  def "should create a new user"() {

        given:

            def role = userRoleDao.create("TestRole1")
            def org = organizationDao.create("TestOrg")
            def roles = ArrayFactory.toLongArray(role.getId())

            def data = JsonStream.serialize(
                new UserRegisterResource("User1", Passwords.nextPassword(24), roles)
            )

        when:

            def mvcResult = mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(data)
            )

        then:
            mvcResult.andExpect(status().is2xxSuccessful())
    }*/
}
