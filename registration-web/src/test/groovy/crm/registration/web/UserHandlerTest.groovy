package crm.registration.web

import crm.registration.web.resources.UserInResource
import crm.security.AccessRole
import crm.security.web.service.UnsafeTokenService
import crm.security.web.service.WebRequestSecurityService
import crm.user.api.dao.UserDao
import com.ss.rlib.common.util.ArrayUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import java.nio.charset.StandardCharsets

import static crm.registration.web.exception.RegistrationErrors.INVALID_BIRTHDAY
import static crm.registration.web.exception.RegistrationErrors.INVALID_BIRTHDAY_MESSAGE
import static crm.registration.web.exception.RegistrationErrors.INVALID_EMAIL
import static crm.registration.web.exception.RegistrationErrors.INVALID_EMAIL_MESSAGE
import static crm.registration.web.exception.RegistrationErrors.INVALID_PASSWORD
import static crm.registration.web.exception.RegistrationErrors.INVALID_PASSWORD_MESSAGE
import static crm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN
import static crm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN_MESSAGE
import static org.hamcrest.Matchers.*

class UserHandlerTest extends RegistrationSpecification {
  
  @Autowired
  UnsafeTokenService unsafeTokenService
  
  @Autowired
  UserDao userDao
  
  def "should create new user"() {
    given:
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(userTestHelper.nextEmail(), org, AccessRole.ORG_ADMIN)
        def token = unsafeTokenService.generateNewToken(user)
        def phoneNumbers = generatePhoneNumbers()
        def messengers = generateMessengers()
        
        def newUser = UserInResource.from(
            "newuser@email.com",
            "First name",
            "Second name",
            "Third name",
            "password".toCharArray(),
            phoneNumbers,
            messengers,
            ArrayUtils.toIntArray((int) AccessRole.ORG_ADMIN.id),
            "1990-05-22"
        )
    
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUser)
            .url("$contextPath/user")
            .exchange()
    then:
        response
            .expectStatus().isCreated()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isNotEmpty()
            .jsonPath('$.email').isEqualTo(newUser.email())
            .jsonPath('$.firstName').isEqualTo(newUser.firstName())
            .jsonPath('$.secondName').isEqualTo(newUser.secondName())
            .jsonPath('$.thirdName').isEqualTo(newUser.thirdName())
            .jsonPath('$.birthday').isEqualTo(newUser.birthday())
            .jsonPath('$.phoneNumbers').value(hasSize(2))
            .jsonPath('$.phoneNumbers[*].countryCode').value(containsInAnyOrder(
            phoneNumbers[0].countryCode(),
            phoneNumbers[1].countryCode()
        ))
            .jsonPath('$.phoneNumbers[*].regionCode').value(containsInAnyOrder(
            phoneNumbers[0].regionCode(),
            phoneNumbers[1].regionCode()
        ))
            .jsonPath('$.phoneNumbers[*].phoneNumber').value(containsInAnyOrder(
            phoneNumbers[0].phoneNumber(),
            phoneNumbers[1].phoneNumber()
        ))
            .jsonPath('$.phoneNumbers[*].type').value(containsInAnyOrder(
            (int) phoneNumbers[0].type(),
            (int) phoneNumbers[1].type()
        ))
            .jsonPath('$.messengers').value(hasSize(2))
            .jsonPath('$.messengers[*].login').value(containsInAnyOrder(
            messengers[0].login(),
            messengers[1].login()
        ))
            .jsonPath('$.messengers[*].type').value(containsInAnyOrder(
            (int) messengers[0].type(),
            (int) messengers[1].type()
        ))
  }
  
  def "should not create new user without required fields"() {
    
    given:
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(userTestHelper.nextEmail(), org, AccessRole.ORG_ADMIN)
        def token = unsafeTokenService.generateNewToken(user)
        def phoneNumbers = generatePhoneNumbers()
        def messengers = generateMessengers()
        
        def newUser = UserInResource.from(
            "newuser@email.com",
            "First name",
            "Second name",
            "Third name",
            "password".toCharArray(),
            phoneNumbers,
            messengers,
            ArrayUtils.toIntArray((int) AccessRole.ORG_ADMIN.id),
            "wefqwefw"
        )
    
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUser)
            .url("$contextPath/user")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_BIRTHDAY, INVALID_BIRTHDAY_MESSAGE)
    when:
        
        newUser = UserInResource.from(
            "newuser@email.com",
            "First name",
            "Second name",
            "Third name",
            "password".toCharArray(),
            phoneNumbers,
            messengers,
            ArrayUtils.toIntArray((int) AccessRole.ORG_ADMIN.id),
            null
        )
        
        response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUser)
            .url("$contextPath/user")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_BIRTHDAY, INVALID_BIRTHDAY_MESSAGE)
    when:
        
        newUser = UserInResource.from(
            "newuser@email.com",
            "First name",
            "Second name",
            "Third name",
            null,
            phoneNumbers,
            messengers,
            ArrayUtils.toIntArray((int) AccessRole.ORG_ADMIN.id),
            "1990-05-22"
        )
        
        response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUser)
            .url("$contextPath/user")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_PASSWORD, INVALID_PASSWORD_MESSAGE)
    when:
        
        newUser = UserInResource.from(
            null,
            "First name",
            "Second name",
            "Third name",
            "password".toCharArray(),
            phoneNumbers,
            messengers,
            ArrayUtils.toIntArray((int) AccessRole.ORG_ADMIN.id),
            "1990-05-22"
        )
        
        response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUser)
            .url("$contextPath/user")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
  }
  
  def "should found that user is exist"() {
    
    given:
        def user = userTestHelper.newUser()
    when:
        def response = webClient.get()
            .url("$contextPath/exist/user/email/$user.email")
            .exchange()
    then:
        response.expectStatus().isOk()
  }
  
  def "should found that user is not exist"() {
    
    given:
        def email = "nonexist@test.com"
    when:
        def response = webClient.get()
            .url("$contextPath/exist/user/email/$email")
            .exchange()
    then:
        response.expectStatus().isNotFound()
  }
  
  def "should bad request if email is invalid"() {
    
    given:
        def email = URLEncoder.encode("@&%&#2", StandardCharsets.UTF_8)
    when:
        def response = webClient.get()
            .url("$contextPath/exist/user/email/$email")
            .exchange()
    then:
        response
            .expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_EMAIL, INVALID_EMAIL_MESSAGE)
  }
  
  def "should found users by name"() {
    
    given:
        def org1 = userTestHelper.newOrganization()
        def org2 = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("user1@mail.com", "FiRst1", "Second1", "Third1", org1)
        userTestHelper.newUser("user2@mail.com", "FIrst2", "Second2", "THird2", org1)
        userTestHelper.newUser("user3@mail.com", "first3", "Second3", "ThIrd3", org1)
        userTestHelper.newUser("user11@mail.com", "First1", "Second1", "Third1", org2)
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/search/user/name/first")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$').value(hasSize(3))
    when:
        response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/search/user/name/first2 second")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$').value(hasSize(1))
            .jsonPath('$[0].firstName').value(is("FIrst2"))
            .jsonPath('$[0].secondName').value(is("Second2"))
    when:
        response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/search/user/name/hird")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$').value(hasSize(3))
    
  }
  
  def "should not found users by name without tokens"() {
    
    when:
        def response = webClient.get()
            .url("$contextPath/search/user/name/first")
            .exchange()
    then:
        response
            .expectStatus().isUnauthorized()
            .verifyErrorResponse(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)
    
  }
  
  def "should load user by id"() {
    
    given:
        def user = userTestHelper.newUser("Admin 1", AccessRole.USER_MANAGER)
        user.firstName = "First name"
        user.secondName = "Second name"
        user.thirdName = "Third name"
        userDao.update(user).block()
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user/$user.id")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$.email').isEqualTo(user.email)
            .jsonPath('$.firstName').isEqualTo(user.firstName)
            .jsonPath('$.secondName').isEqualTo(user.secondName)
            .jsonPath('$.thirdName').isEqualTo(user.thirdName)
            .jsonPath('$.created').isEqualTo(user.created.toEpochMilli())
            .jsonPath('$.modified').isEqualTo(user.modified.toEpochMilli())
  }
  
  def "should load minimal user presentation by id"() {
    
    given:
        def user = userTestHelper.newUser("Not admin")
        user.firstName = "First name"
        user.secondName = "Second name"
        user.thirdName = "Third name"
        userDao.update(user).block()
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user/minimal/$user.id")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$.email').isEqualTo(user.email)
            .jsonPath('$.firstName').isEqualTo(user.firstName)
            .jsonPath('$.secondName').isEqualTo(user.secondName)
            .jsonPath('$.thirdName').isEqualTo(user.thirdName)
    when:
        response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user/$user.id")
            .exchange()
    then:
        response.expectStatus().isForbidden()
  }
  
  def "should not load user by id without token or under other organization"() {
    
    given:
        def org1 = userTestHelper.newOrganization()
        def org2 = userTestHelper.newOrganization()
        def user1 = userTestHelper.newUser("TestUser1", org1, AccessRole.USER_MANAGER)
        def user2 = userTestHelper.newUser("TestUser2", org2)
        def token = unsafeTokenService.generateNewToken(user1)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user/$user2.id")
            .exchange()
    then:
        response.expectStatus().isNotFound()
    when:
        response = webClient.get()
            .url("$contextPath/user/$user1.id")
            .exchange()
    then:
        response.expectStatus().isUnauthorized()
            .verifyErrorResponse(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)
  }
  
  def "should load users by ids"() {
    
    given:
        def org1 = userTestHelper.newOrganization()
        def org2 = userTestHelper.newOrganization()
        def user1 = userTestHelper.newUser(userTestHelper.nextEmail(), org1, AccessRole.USER_MANAGER)
        def user2 = userTestHelper.newUser(org1)
        def user3 = userTestHelper.newUser(org1)
        def user4 = userTestHelper.newUser(org2)
        def token = unsafeTokenService.generateNewToken(user1)
        
        long[] ids = [user1.id, user2.id, user3.id]
    
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/users/ids")
            .body(ids)
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$').value(hasSize(3))
            .jsonPath('$[*].id').value(containsInAnyOrder(
            (int) user1.id, (int) user2.id, (int) user3.id
        ))
  }
  
  def "should load page of users successfully"() {
    given:
        
        def firstOrgContactsCount = 20
        def secondOrgContactsCount = 5
        
        def firstOrganization = userTestHelper.newOrganization()
        def secondOrganization = userTestHelper.newOrganization()
        
        def firstUser = userTestHelper.newUser(userTestHelper.nextEmail(),
            firstOrganization,
            AccessRole.USER_MANAGER)
        
        (firstOrgContactsCount - 1).times {
          userTestHelper.newUser(userTestHelper.nextEmail(), firstOrganization)
        }
        
        secondOrgContactsCount.times {
          userTestHelper.newUser(userTestHelper.nextEmail(), secondOrganization)
        }
        
        def token = unsafeTokenService.generateNewToken(firstUser)
    
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/users/page?pageSize=5&offset=0")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.totalSize').isEqualTo(20)
            .jsonPath('$.resources').value(hasSize(5))
    when:
        response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/users/page?pageSize=10&offset=12")
            .exchange()
    then:
        response.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.totalSize').isEqualTo(20)
            .jsonPath('$.resources').value(hasSize(8))
  }
  
  def "should load minimal users by ids"() {
    
    given:
        def org1 = userTestHelper.newOrganization()
        def org2 = userTestHelper.newOrganization()
        def user1 = userTestHelper.newUser(userTestHelper.nextEmail(), org1)
        def user2 = userTestHelper.newUser(userTestHelper.nextEmail(), org1)
        def user3 = userTestHelper.newUser(userTestHelper.nextEmail(), org1)
        def user4 = userTestHelper.newUser(userTestHelper.nextEmail(), org2)
        def token = unsafeTokenService.generateNewToken(user1)
        
        long[] ids = [user1.id, user2.id, user3.id]
    
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/users/minimal/ids")
            .body(ids)
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectBody()
            .jsonPath('$').isNotEmpty()
            .jsonPath('$').value(hasSize(3))
            .jsonPath('$[*].id').value(containsInAnyOrder(
            (int) user1.id, (int) user2.id, (int) user3.id
        ))
  }
}
