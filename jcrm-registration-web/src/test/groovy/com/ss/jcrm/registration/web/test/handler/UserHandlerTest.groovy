package com.ss.jcrm.registration.web.test.handler

import com.ss.jcrm.registration.web.resources.UserInResource
import com.ss.jcrm.registration.web.test.RegistrationSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN_MESSAGE
import static org.hamcrest.Matchers.*

class UserHandlerTest extends RegistrationSpecification {
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    @Autowired
    UserDao userDao
    
    def "should create a new user"() {
        
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
        
            def newUser = UserInResource.newResource(
                "newuser@email.com",
                "First name",
                "Second name",
                "Third name",
                "password".toCharArray(),
                new PhoneNumberResource(
                
                ),
                AccessRole.ORG_ADMIN,
                "1990-05-22"
            )
        
        when:
            def response = client.post()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(newUser)
                .url("/user")
                .exchange()
        then:
            response.expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath('$.id').isNotEmpty()
                .jsonPath('$.assigner').isEqualTo((int) body.assigner)
                .jsonPath('$.curators[*]').value(containsInAnyOrder(
                (int) body.curators[0],
                (int) body.curators[1]
            ))
                .jsonPath('$.firstName').isEqualTo(body.firstName)
                .jsonPath('$.secondName').isEqualTo(body.secondName)
                .jsonPath('$.thirdName').isEqualTo(body.thirdName)
                .jsonPath('$.company').isEqualTo(body.company)
                .jsonPath('$.birthday').isEqualTo(body.birthday)
                .jsonPath('$.created').isNotEmpty()
                .jsonPath('$.modified').isNotEmpty()
                .jsonPath('$.phoneNumbers').value(hasSize(1))
                .jsonPath('$.phoneNumbers[0].countryCode').isEqualTo(body.phoneNumbers[0].countryCode)
                .jsonPath('$.phoneNumbers[0].regionCode').isEqualTo(body.phoneNumbers[0].regionCode)
                .jsonPath('$.phoneNumbers[0].phoneNumber').isEqualTo(body.phoneNumbers[0].phoneNumber)
                .jsonPath('$.phoneNumbers[0].type').value(is(body.phoneNumbers[0].type))
                .jsonPath('$.emails').value(hasSize(1))
                .jsonPath('$.emails[0].type').isEqualTo(body.emails[0].type)
                .jsonPath('$.emails[0].email').isEqualTo(body.emails[0].email)
                .jsonPath('$.sites').value(hasSize(2))
                .jsonPath('$.sites[*].type').value(containsInAnyOrder(
                body.sites[0].type,
                body.sites[1].type
            ))
                .jsonPath('$.sites[*].url').value(containsInAnyOrder(
                body.sites[0].url,
                body.sites[1].url
            ))
                .jsonPath('$.messengers').value(hasSize(2))
                .jsonPath('$.messengers[*].type').value(containsInAnyOrder(
                body.messengers[0].type,
                body.messengers[1].type
            ))
                .jsonPath('$.messengers[*].login').value(containsInAnyOrder(
                body.messengers[0].login,
                body.messengers[1].login
            ))
    }
    
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
            def user = userTestHelper.newUser("Admin 1", AccessRole.USER_MANAGER)
            user.phoneNumber = "testphonenumber"
            user.firstName = "First name"
            user.secondName = "Second name"
            user.thirdName = "Third name"
            userDao.update(user).block()
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
                    .jsonPath('$.email').isEqualTo(user.email)
                    .jsonPath('$.firstName').isEqualTo(user.firstName)
                    .jsonPath('$.secondName').isEqualTo(user.secondName)
                    .jsonPath('$.thirdName').isEqualTo(user.thirdName)
                    .jsonPath('$.phoneNumber').isEqualTo(user.phoneNumber)
                    .jsonPath('$.created').isEqualTo(user.created.toEpochMilli())
                    .jsonPath('$.modified').isEqualTo(user.modified.toEpochMilli())
    }
    
    def "should load minimal user presentation by id"() {
        
        given:
            def user = userTestHelper.newUser("Not admin")
            user.phoneNumber = "testphonenumber"
            user.firstName = "First name"
            user.secondName = "Second name"
            user.thirdName = "Third name"
            userDao.update(user).block()
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user/minimal/$user.id")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectBody()
                    .jsonPath('$').isNotEmpty()
                    .jsonPath('$.email').isEqualTo(user.email)
                    .jsonPath('$.firstName').isEqualTo(user.firstName)
                    .jsonPath('$.secondName').isEqualTo(user.secondName)
                    .jsonPath('$.thirdName').isEqualTo(user.thirdName)
                    .jsonPath('$.phoneNumber').isEqualTo(user.phoneNumber)
        when:
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/user/$user.id")
                .exchange()
        then:
            response.expectStatus().isForbidden()
    }
    
    def "should not load user by id without token or under other organization"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user1 = userTestHelper.newUser("TestUser1", org1, AccessRole.USER_MANAGER)
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
            def user1 = userTestHelper.newUser("TestUser1", org1, AccessRole.USER_MANAGER)
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
    
    def "should load a page of contacts successfully"() {
        
        given:
    
            def firstOrgContactsCount = 20
            def secondOrgContactsCount = 5
    
            def firstOrg = userTestHelper.newOrg()
            def firstUser = userTestHelper.newUser("User1", firstOrg, AccessRole.USER_MANAGER)
    
            def secondOrg = userTestHelper.newOrg()
    
            (firstOrgContactsCount - 1).times { userTestHelper.newUser(userTestHelper.nextUId(), firstOrg) }
            secondOrgContactsCount.times { userTestHelper.newUser(userTestHelper.nextUId(), secondOrg) }
    
            def token = unsafeTokenService.generateNewToken(firstUser)
        
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/users/page?pageSize=5&offset=0")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(20)
                    .jsonPath('$.resources').value(hasSize(5))
        when:
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/registration/users/page?pageSize=10&offset=12")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(20)
                    .jsonPath('$.resources').value(hasSize(8))
    }
    
    def "should load minimal users by ids"() {
        
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
                .url("/registration/users/minimal/ids")
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
