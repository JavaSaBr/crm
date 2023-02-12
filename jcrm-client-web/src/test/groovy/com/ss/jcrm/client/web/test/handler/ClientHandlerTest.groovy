package com.ss.jcrm.client.web.test.handler

import com.ss.jcrm.client.web.test.ClientSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import com.ss.rlib.common.util.StringUtils
import com.ss.rlib.common.util.array.ArrayFactory

import con.ss.jcrm.client.web.resource.ClientInResource
import crm.contact.api.EmailType
import crm.contact.api.MessengerType
import crm.contact.api.PhoneNumberType
import crm.contact.api.SiteType
import crm.contact.api.resource.EmailResource
import crm.contact.api.resource.MessengerResource
import crm.contact.api.resource.PhoneNumberResource
import crm.contact.api.resource.SiteResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

import static crm.base.web.exception.CommonErrors.ID_NOT_PRESENTED
import static crm.base.web.exception.CommonErrors.ID_NOT_PRESENTED_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_ASSIGNER_NOT_PRESENTED
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_ASSIGNER_NOT_PRESENTED_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_BIRTHDAY_INVALID
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_BIRTHDAY_INVALID_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_COMPANY_TOO_LONG
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_COMPANY_TOO_LONG_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_EMAIL_INVALID
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_EMAIL_INVALID_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_FIRST_NAME_INVALID_LENGTH
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_MESSENGER_INVALID
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_MESSENGER_INVALID_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_PHONE_NUMBER_INVALID
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_PHONE_NUMBER_INVALID_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_SECOND_NAME_INVALID_LENGTH
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_SITE_INVALID
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_SITE_INVALID_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_THIRD_NAME_TOO_LONG
import static con.ss.jcrm.client.web.exception.ClientErrors.CLIENT_THIRD_NAME_TOO_LONG_MESSAGE
import static con.ss.jcrm.client.web.exception.ClientErrors.INVALID_ASSIGNER
import static con.ss.jcrm.client.web.exception.ClientErrors.INVALID_ASSIGNER_MESSAGE
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class ClientHandlerTest extends ClientSpecification {
  
  @Autowired
  UnsafeTokenService unsafeTokenService
  
  def "should create new client successfully"() {
    given:
        def organization = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("User1", organization, AccessRole.ORG_ADMIN)
        def assigner = userTestHelper.newUser("Assigner1", organization)
        def curator1 = userTestHelper.newUser("Curator1", organization)
        def curator2 = userTestHelper.newUser("Curator2", organization)
        def token = unsafeTokenService.generateNewToken(user)
    
        def body = new ClientInResource(
            0,
            ArrayFactory.toLongArray(curator1.id(), curator2.id()),
            assigner.id(),
            0,
            "First name",
            "Second name",
            "Third name",
            "Company",
            "1990-05-22",
            List.of(ClientPhoneNumberResource.of("+7", "234", "123132", PhoneNumberType.WORK)),
            List.of(ClientEmailResource.of("Test@test.com", EmailType.HOME)),
            List.of(
                ClientSiteResource.of("work.site.com", SiteType.WORK),
                ClientSiteResource.of("home.site.com", SiteType.HOME)),
            List.of(
                ClientMessengerResource.of("misterX", MessengerType.TELEGRAM),
                ClientMessengerResource.of("misterX", MessengerType.SKYPE)))
    when:
        def response = webClient.post()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(body)
            .url("$contextPath/client")
            .exchange()
    then:
        response.expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isNotEmpty()
            .jsonPath('$.assigner').isEqualTo((int) body.assigner())
            .jsonPath('$.curators[*]').value(containsInAnyOrder((int) body.curators()[0], (int) body.curators()[1]))
            .jsonPath('$.firstName').isEqualTo(body.firstName())
            .jsonPath('$.secondName').isEqualTo(body.secondName())
            .jsonPath('$.thirdName').isEqualTo(body.thirdName())
            .jsonPath('$.company').isEqualTo(body.company())
            .jsonPath('$.birthday').isEqualTo(body.birthday())
            .jsonPath('$.created').isNotEmpty()
            .jsonPath('$.modified').isNotEmpty()
            .jsonPath('$.phoneNumbers').value(hasSize(1))
            .jsonPath('$.phoneNumbers[0].countryCode').isEqualTo(body.phoneNumbers()[0].countryCode())
            .jsonPath('$.phoneNumbers[0].regionCode').isEqualTo(body.phoneNumbers()[0].regionCode())
            .jsonPath('$.phoneNumbers[0].phoneNumber').isEqualTo(body.phoneNumbers()[0].phoneNumber())
            .jsonPath('$.phoneNumbers[0].type').value(is(body.phoneNumbers()[0].type))
            .jsonPath('$.emails').value(hasSize(1))
            .jsonPath('$.emails[0].type').isEqualTo(body.emails()[0].type())
            .jsonPath('$.emails[0].email').isEqualTo(body.emails()[0].email())
            .jsonPath('$.sites').value(hasSize(2))
            .jsonPath('$.sites[*].type')
              .value(containsInAnyOrder(body.sites()[0].type(), body.sites()[1].type()))
            .jsonPath('$.sites[*].url')
              .value(containsInAnyOrder(body.sites()[0].url(), body.sites()[1].url()))
            .jsonPath('$.messengers').value(hasSize(2))
            .jsonPath('$.messengers[*].type')
              .value(containsInAnyOrder(body.messengers()[0].type(), body.messengers()[1].type()))
            .jsonPath('$.messengers[*].login')
              .value(containsInAnyOrder(body.messengers()[0].login(), body.messengers()[1].login()))
  }
  
  def "should get all available clients for org successfully"() {
    given:
        def user = userTestHelper.newUser("User2", AccessRole.ORG_ADMIN)
        def token = unsafeTokenService.generateNewToken(user)
        def client1 = clientTestHelper.newSimpleClient(user)
        def client2 = clientTestHelper.newSimpleClient(user)
        def client3 = clientTestHelper.newSimpleClient(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/clients")
            .exchange()
    then:
        response.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$').value(hasSize(3))
            .jsonPath('$[*].id')
              .value(containsInAnyOrder((int) client1.id(), (int) client2.id(), (int) client3.id()))
            .jsonPath('$[*].firstName')
              .value(containsInAnyOrder(client1.firstName(), client2.firstName(), client3.firstName()))
  }
  
  def "should load created client successfully"() {
    given:
        def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
        def token = unsafeTokenService.generateNewToken(user)
        def client = clientTestHelper.newSimpleClient(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/client/${client.id()}")
            .exchange()
    then:
        response.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isNotEmpty()
            .jsonPath('$.firstName').isEqualTo(client.firstName())
            .jsonPath('$.secondName').isEqualTo(client.secondName())
            .jsonPath('$.thirdName').isEqualTo(client.thirdName())
  }
  
  def "should failed when id is not presented"() {
    given:
        def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
        def token = unsafeTokenService.generateNewToken(user)
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/client/invallid")
            .exchange()
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(ID_NOT_PRESENTED, ID_NOT_PRESENTED_MESSAGE)
  }
  
  def "should failed creating invalid client"() {
    given:
        def organization = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("User1", organization, AccessRole.ORG_ADMIN)
        def assigner = userTestHelper.newUser("Assigner1", organization)
        def token = unsafeTokenService.generateNewToken(user)
        def body = ClientInResource.empty()
    when:
        def response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_ASSIGNER_NOT_PRESENTED, CLIENT_ASSIGNER_NOT_PRESENTED_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            null,
            null,
            null,
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_FIRST_NAME_INVALID_LENGTH, CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            StringUtils.generate(1000),
            null,
            null,
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_FIRST_NAME_INVALID_LENGTH, CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            null,
            null,
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_SECOND_NAME_INVALID_LENGTH, CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            StringUtils.generate(1000),
            null,
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_SECOND_NAME_INVALID_LENGTH, CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            StringUtils.generate(1000),
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_THIRD_NAME_TOO_LONG, CLIENT_THIRD_NAME_TOO_LONG_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            StringUtils.generate(1000),
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_COMPANY_TOO_LONG, CLIENT_COMPANY_TOO_LONG_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "invaliddate")
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_BIRTHDAY_INVALID, CLIENT_BIRTHDAY_INVALID_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1700-05-10")
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_BIRTHDAY_INVALID, CLIENT_BIRTHDAY_INVALID_MESSAGE)
    when:
        body = ClientInResource.withoutContacts(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "2500-05-10")
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_BIRTHDAY_INVALID, CLIENT_BIRTHDAY_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            List.of(new PhoneNumberResource("+7", "234", "1234567", -1)),
            null,
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_PHONE_NUMBER_INVALID, CLIENT_PHONE_NUMBER_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            List.of(ClientPhoneNumberResource.of("+1234556", "12345678", "123456789", PhoneNumberType.MOBILE)),
            null,
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_PHONE_NUMBER_INVALID, CLIENT_PHONE_NUMBER_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            List.of(new EmailResource("Test@test.com", -1)),
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_EMAIL_INVALID, CLIENT_EMAIL_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            List.of(EmailResource.of("invalid", EmailType.HOME)),
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_EMAIL_INVALID, CLIENT_EMAIL_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            List.of(EmailResource.of(StringUtils.generate(1000), EmailType.HOME)),
            null,
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_EMAIL_INVALID, CLIENT_EMAIL_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            null,
            List.of(new SiteResource("work.site.com", -1)),
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_SITE_INVALID, CLIENT_SITE_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            null,
            List.of(SiteResource.of(StringUtils.generate(1000), SiteType.WORK)),
            null)
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_SITE_INVALID, CLIENT_SITE_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            null,
            null,
            List.of(new MessengerResource("misterX", -1)))
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_MESSENGER_INVALID, CLIENT_MESSENGER_INVALID_MESSAGE)
    when:
        body = new ClientInResource(0,
            null,
            assigner.id() as long,
            0,
            "First Name",
            "Second name",
            null,
            null,
            "1950-04-22",
            null,
            null,
            null,
            List.of(MessengerResource.of(StringUtils.generate(1000), MessengerType.TELEGRAM)))
        response = sendCreateRequest(token, body)
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(CLIENT_MESSENGER_INVALID, CLIENT_MESSENGER_INVALID_MESSAGE)
  }
  
  private WebTestClient.ResponseSpec sendCreateRequest(String token, ClientInResource body) {
    webClient.post()
        .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
        .body(body)
        .url("$contextPath/client")
        .exchange()
  }
  
  def "should load page of clients successfully"() {
    given:
        def firstOrgContactsCount = 20
        def secondOrgContactsCount = 5
        def firstOrg = userTestHelper.newOrganization()
        def firstUser = userTestHelper.newUser("User1", firstOrg)
        def secondOrg = userTestHelper.newOrganization()
        def secondUser = userTestHelper.newUser("User2", secondOrg)
        
        firstOrgContactsCount.times {
          clientTestHelper.newSimpleClient(firstUser)
        }
        
        secondOrgContactsCount.times {
          clientTestHelper.newSimpleClient(secondUser)
        }
        
        def token = unsafeTokenService.generateNewToken(firstUser)
    
    when:
        def response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/clients/page?pageSize=5&offset=0")
            .exchange()
    then:
        response.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.totalSize').isEqualTo(20)
            .jsonPath('$.resources').value(hasSize(5))
    when:
        response = webClient.get()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .url("$contextPath/clients/page?pageSize=10&offset=12")
            .exchange()
    then:
        response.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.totalSize').isEqualTo(20)
            .jsonPath('$.resources').value(hasSize(8))
  }
  
  def "should update client successfully"() {
    
    given:
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
        def assigner = userTestHelper.newUser("Assigner1", org)
        def curator1 = userTestHelper.newUser("Curator1", org)
        def curator2 = userTestHelper.newUser("Curator2", org)
        def newClient = clientTestHelper.newSimpleClient(user)
        
        def token = unsafeTokenService.generateNewToken(user)
        def body = new ClientInResource(
            newClient.id(), [
            curator1.id(),
            curator2.id()
        ] as long[],
            assigner.id() as long,
            newClient.version(),
            "First name",
            "Second name",
            "Third name",
            "Company",
            "1990-05-22", [
            PhoneNumberResource.of("+7", "234", "123132", PhoneNumberType.WORK)
        ], [
            EmailResource.of("Test@test.com", EmailType.HOME)
        ], [
            SiteResource.of("work.site.com", SiteType.WORK),
            SiteResource.of("home.site.com", SiteType.HOME)
        ], [
            MessengerResource.of("mist" + "erX", MessengerType.TELEGRAM),
            MessengerResource.of("misterX", MessengerType.SKYPE)
        ])
    
    when:
        def response = webClient.put()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(body)
            .url("$contextPath/client")
            .exchange()
    then:
        response.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath('$.id').isNotEmpty()
            .jsonPath('$.assigner').isEqualTo((int) body.assigner())
            .jsonPath('$.curators[*]').value(containsInAnyOrder((int) body.curators()[0],
            (int) body.curators()[1]))
            .jsonPath('$.firstName').isEqualTo(body.firstName())
            .jsonPath('$.secondName').isEqualTo(body.secondName())
            .jsonPath('$.thirdName').isEqualTo(body.thirdName())
            .jsonPath('$.company').isEqualTo(body.company())
            .jsonPath('$.birthday').isEqualTo(body.birthday())
            .jsonPath('$.created').isNotEmpty()
            .jsonPath('$.modified').isNotEmpty()
            .jsonPath('$.phoneNumbers').value(hasSize(1))
            .jsonPath('$.phoneNumbers[0].countryCode').isEqualTo(body.phoneNumbers()[0].countryCode())
            .jsonPath('$.phoneNumbers[0].regionCode').isEqualTo(body.phoneNumbers()[0].regionCode())
            .jsonPath('$.phoneNumbers[0].phoneNumber').isEqualTo(body.phoneNumbers()[0].phoneNumber())
            .jsonPath('$.phoneNumbers[0].type').value(is(body.phoneNumbers()[0].type()))
            .jsonPath('$.emails').value(hasSize(1))
            .jsonPath('$.emails[0].type').isEqualTo(body.emails()[0].type())
            .jsonPath('$.emails[0].email').isEqualTo(body.emails()[0].email())
            .jsonPath('$.sites').value(hasSize(2))
            .jsonPath('$.sites[*].type').value(containsInAnyOrder(body.sites()[0].type(),
            body.sites()[1].type()))
            .jsonPath('$.sites[*].url').value(containsInAnyOrder(body.sites()[0].url(),
            body.sites()[1].url()))
            .jsonPath('$.messengers').value(hasSize(2))
            .jsonPath('$.messengers[*].type').value(containsInAnyOrder(body.messengers()[0].type(),
            body.messengers()[1].type()))
            .jsonPath('$.messengers[*].login').value(containsInAnyOrder(body.messengers()[0].login(),
            body.messengers()[1].login()))
  }
  
  def "should failed updating client"() {
    
    given:
        
        def org = userTestHelper.newOrganization()
        def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
        def assigner = userTestHelper.newUser("Assigner1", org)
        def client = clientTestHelper.newSimpleClient(user)
        
        def token = unsafeTokenService.generateNewToken(user)
        def body = ClientInResource.withoutContacts(client.id,
            null,
            assigner.id,
            10,
            "First name",
            "Second name",
            "Third name",
            null,
            null)
    
    when:
        def response = webClient.put()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(body)
            .url("$contextPath/client")
            .exchange()
    then:
        response.expectStatus().isEqualTo(HttpStatus.CONFLICT)
    when:
        
        body = ClientInResource.withoutContacts(client.id,
            null,
            (assigner.id + 100),
            client.version,
            "First name",
            "Second name",
            "Third name",
            null,
            null)
        
        response = webClient.put()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(body)
            .url("$contextPath/client")
            .exchange()
    then:
        response.expectStatus().isBadRequest()
            .verifyErrorResponse(INVALID_ASSIGNER, INVALID_ASSIGNER_MESSAGE)
  }
}
