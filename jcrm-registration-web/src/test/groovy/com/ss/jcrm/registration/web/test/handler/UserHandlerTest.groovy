package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.security.web.exception.SecurityErrors
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import org.springframework.beans.factory.annotation.Autowired

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN_MESSAGE
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class UserHandlerTest extends RegistrationSpecification {
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    def "should found that user is exist"() {

        given:
            def user = userTestHelper.newUser()
        when:
            def response = client.get()
                .url("/registration/exist/user/email/$user.email")
                .exchange()
        then:
            response.expectStatus().isOk()
    }

    def "should found that user is not exist"() {
    
        given:
            def email = "nonexist@test.com"
        when:
            def response = client.get()
                .url("/registration/exist/user/email/$email")
                .exchange()
        then:
            response.expectStatus().isNotFound()
    }
    
    def "should bad request if email invalid"() {
    
        given:
            def email = "@&%&#2"
        when:
            def response = client.get()
                .url("/registration/exist/user/email/$email")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
    }
    
    def "should found users by name"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user = userTestHelper.newUser("user1@mail.com", "FiRst1", "Second1", "Third1", org1)
            userTestHelper.newUser("user2@mail.com", "FIrst2", "Second2", "THird2", org1)
            userTestHelper.newUser("user3@mail.com", "first3", "Second3", "ThIrd3", org1)
            userTestHelper.newUser("user11@mail.com", "First1", "Second1", "Third1", org2)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/search/user/name/first")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$').isNotEmpty()
                    .jsonPath('$').value(hasSize(3))
        when:
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/search/user/name/first2 second")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$').isNotEmpty()
                    .jsonPath('$').value(hasSize(1))
                    .jsonPath('$[0].firstName').value(is("FIrst2"))
                    .jsonPath('$[0].secondName').value(is("Second2"))
        when:
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/search/user/name/hird")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$').isNotEmpty()
                    .jsonPath('$').value(hasSize(3))
    
    }
    
    def "should not found users by name without tokens"() {
        
        when:
            def response = client.get()
                .url("/registration/search/user/name/first")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)
        
    }
    
    def "should load user by id"() {
        
        given:
            def user = userTestHelper.newUser()
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user/$user.id")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$').isNotEmpty()
                    .jsonPath('$.email').value(is(user.email))
    }
    
    def "should not load user by id without token or under other organization"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user1 = userTestHelper.newUser("TestUser1", org1)
            def user2 = userTestHelper.newUser("TestUser2", org2)
            def token = unsafeTokenService.generateNewToken(user1)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user/$user2.id")
                .exchange()
        then:
            response.expectStatus().isNotFound()
        when:
            response = client.get()
                .url("/registration/user/$user1.id")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)
    }
    
    def "should load users by ids"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user1 = userTestHelper.newUser("TestUser1", org1)
            def user2 = userTestHelper.newUser("TestUser2", org1)
            def user3 = userTestHelper.newUser("TestUser3", org1)
            def user4 = userTestHelper.newUser("TestUser4", org2)
            def token = unsafeTokenService.generateNewToken(user1)
        
            long[] ids = [user1.id, user2.id, user3.id]
        
        when:
            def response = client.post()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/users/ids")
                .body(ids)
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$').isNotEmpty()
                    .jsonPath('$').value(hasSize(3))
                    .jsonPath('$[*].id').value(containsInAnyOrder(
                        (int) user1.id, (int) user2.id, (int) user3.id)
                    )
    }
}
