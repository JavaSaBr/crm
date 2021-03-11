package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.resources.UserGroupInResource
import com.ss.jcrm.registration.web.resources.UserGroupOutResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.hamcrest.Matchers.*

class UserGroupHandlerTest extends RegistrationSpecification {
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    @Autowired
    UserDao userDao
    
    @Autowired
    UserGroupDao userGroupDao
    
    def "should create new user group with users and roles"() {
        
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def user1 = userTestHelper.newUser("User1inGroup", org, AccessRole.CREATE_USER)
            def user2 = userTestHelper.newUser("User2inGroup", org, AccessRole.DELETE_USER)
        
            def newUserGroup = UserGroupInResource.newResource(
                "group1",
                [
                    AccessRole.USER_GROUP_MANAGER.id,
                    AccessRole.USER_MANAGER.id
                ] as int[],
                [
                    user1.id,
                    user2.id
                ] as long[],
            )
    
            def token = unsafeTokenService.generateNewToken(user)
        
        when:
            def response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(newUserGroup)
                .url("/registration/user-group")
                .exchange()
        then:
            def content = response
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.id').isNotEmpty()
                    .jsonPath('$.name').isEqualTo("group1")
                    .jsonPath('$.roles[*]').value(containsInAnyOrder(
                        AccessRole.USER_GROUP_MANAGER.id as int,
                        AccessRole.USER_MANAGER.id as int
                    ))
                .returnResult()
        when:
    
            def resource = objectMapper.readValue(content.responseBody, UserGroupOutResource)
        
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user-group/${resource.id}/users/page?pageSize=2&offset=0")
                .exchange()
        
        then:
            response
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(2)
                    .jsonPath('$.resources').value(hasSize(2))
                    .jsonPath('$.resources[*].id').value(containsInAnyOrder(
                        user1.id as int,
                        user2.id as int
                    ))
    }
    
    def "should fetch page of groups in organization"() {
        
        given:
            
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org1, AccessRole.ORG_ADMIN)
        
            def group1 = userTestHelper.newGroup(org1)
            def group2 = userTestHelper.newGroup(org1)
            def group3 = userTestHelper.newGroup(org1)
            def group4 = userTestHelper.newGroup(org2)
            def group5 = userTestHelper.newGroup(org2)
    
            def token = unsafeTokenService.generateNewToken(user)
        
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user-groups/page?pageSize=10&offset=0")
                .exchange()
        then:
            response
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(3)
                    .jsonPath('$.resources').value(hasSize(3))
                    .jsonPath('$.resources[*].id').value(containsInAnyOrder(
                        group1.id as int,
                        group2.id as int,
                        group3.id as int,
                    ))
        when:
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user-groups/page?pageSize=1&offset=0")
                .exchange()
        then:
            response
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(3)
                    .jsonPath('$.resources').value(hasSize(1))
                    .jsonPath('$.resources[0].id').isEqualTo(group1.id as int)
    }
    
    def "should found that user group is exist in user's org"() {
        
        given:
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User", org, AccessRole.ORG_ADMIN)
            def group = userTestHelper.newGroup("Group123", org)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/exist/user-group/name/$group.name")
                .exchange()
        then:
            response.expectStatus().isOk()
    }
    
    def "should found that user group is not exist in user's org"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User", org1, AccessRole.ORG_ADMIN)
            def group = userTestHelper.newGroup("Group123", org2)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/exist/user-group/name/$group.name")
                .exchange()
        then:
            response.expectStatus().isNotFound()
    }
}
