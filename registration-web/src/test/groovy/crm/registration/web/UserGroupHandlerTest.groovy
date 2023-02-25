package crm.registration.web

import crm.registration.web.resources.UserGroupInResource
import crm.registration.web.RegistrationSpecification
import crm.security.AccessRole
import crm.security.web.service.UnsafeTokenService
import crm.security.web.service.WebRequestSecurityService
import crm.user.api.dao.UserDao
import crm.user.api.dao.UserGroupDao
import com.ss.rlib.common.util.ArrayUtils
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
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
        
        def newUserGroup = UserGroupInResource.from(
            "group1",
            [
                AccessRole.USER_GROUP_MANAGER.id,
                AccessRole.USER_MANAGER.id
            ] as long[]
        )
        
        def token = unsafeTokenService.generateNewToken(user)
    
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUserGroup)
            .url("$contextPath/user-group")
            .exchange()
    then:
        response
            .expectStatus().isCreated()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isNotEmpty()
            .jsonPath('$.name').isEqualTo("group1")
            .jsonPath('$.modified').exists()
            .jsonPath('$.created').exists()
            .jsonPath('$.roles[*]').value(containsInAnyOrder(
            AccessRole.USER_GROUP_MANAGER.id as int,
            AccessRole.USER_MANAGER.id as int
        ))
  }
  
  def "should create new user group without users and roles"() {
    
    given:
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(userTestHelper.nextEmail(), org, AccessRole.ORG_ADMIN)
        
        def newUserGroup = UserGroupInResource.from(userTestHelper.nextGroupName(), ArrayUtils.EMPTY_LONG_ARRAY)
        def token = unsafeTokenService.generateNewToken(user)
    
    when:
        def response = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(newUserGroup)
            .url("$contextPath/user-group")
            .exchange()
    then:
        response
            .expectStatus().isCreated()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isNotEmpty()
            .jsonPath('$.name').isEqualTo(newUserGroup.name())
            .jsonPath('$.modified').exists()
            .jsonPath('$.created').exists()
            .jsonPath('$.roles').value(hasSize(0))
            .returnResult()
  }
  
  def "should load group by id"() {
    
    given:
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(userTestHelper.nextEmail(), org, AccessRole.VIEW_USER_GROUPS)
        def group = userTestHelper.newGroup(userTestHelper.nextGroupName(), org, Set.of(
            AccessRole.CREATE_USER,
            AccessRole.VIEW_USERS
        ))
        def token = unsafeTokenService.generateNewToken(user)
    
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user-group/$group.id")
            .exchange()
    then:
        response
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isEqualTo(group.id)
            .jsonPath('$.name').isEqualTo(group.name)
            .jsonPath('$.modified').isEqualTo(group.modified.toEpochMilli())
            .jsonPath('$.created').isEqualTo(group.created.toEpochMilli())
            .jsonPath('$.roles').value(hasSize(2))
            .jsonPath('$.roles[*]').value(containsInAnyOrder(
            AccessRole.CREATE_USER.id as int,
            AccessRole.VIEW_USERS.id as int,
        ))
  }
  
  def "should fetch page of groups in organization"() {
    
    given:
        
        def org1 = userTestHelper.newOrganization()
        def org2 = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(userTestHelper.nextEmail(), org1, AccessRole.ORG_ADMIN)
        
        def group1 = userTestHelper.newGroup(org1)
        def group2 = userTestHelper.newGroup(org1)
        def group3 = userTestHelper.newGroup(org1)
        def group4 = userTestHelper.newGroup(org2)
        def group5 = userTestHelper.newGroup(org2)
        
        def token = unsafeTokenService.generateNewToken(user)
    
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user-groups/page?pageSize=10&offset=0")
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
        response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/user-groups/page?pageSize=1&offset=0")
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
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("User", org, AccessRole.ORG_ADMIN)
        def group = userTestHelper.newGroup("Group123", org)
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/exist/user-group/name/$group.name")
            .exchange()
    then:
        response.expectStatus().isOk()
  }
  
  def "should found that user group is not exist in user's org"() {
    
    given:
        def org1 = userTestHelper.newOrganization()
        def org2 = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(userTestHelper.nextEmail(), org1, AccessRole.ORG_ADMIN)
        def group = userTestHelper.newGroup(userTestHelper.nextGroupName(), org2)
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/exist/user-group/name/$group.name")
            .exchange()
    then:
        response.expectStatus().isNotFound()
  }
}
