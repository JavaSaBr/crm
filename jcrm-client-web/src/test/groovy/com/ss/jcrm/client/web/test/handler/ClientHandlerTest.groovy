package com.ss.jcrm.client.web.test.handler

import com.ss.jcrm.client.api.EmailType
import com.ss.jcrm.client.api.MessengerType
import com.ss.jcrm.client.api.PhoneNumberType
import com.ss.jcrm.client.api.SiteType
import com.ss.jcrm.client.web.test.ClientSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import com.ss.rlib.common.util.StringUtils
import con.ss.jcrm.client.web.resource.ClientEmailResource
import con.ss.jcrm.client.web.resource.ClientInResource
import con.ss.jcrm.client.web.resource.ClientMessengerResource
import con.ss.jcrm.client.web.resource.ClientPhoneNumberResource
import con.ss.jcrm.client.web.resource.ClientSiteResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

import static com.ss.jcrm.web.exception.CommonErrors.ID_NOT_PRESENTED
import static com.ss.jcrm.web.exception.CommonErrors.ID_NOT_PRESENTED_MESSAGE
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
    
    def "should create a new contact successfully"() {
    
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def assigner = userTestHelper.newUser("Assigner1", org)
            def curator1 = userTestHelper.newUser("Curator1", org)
            def curator2 = userTestHelper.newUser("Curator2", org)
            
            def token = unsafeTokenService.generateNewToken(user)
            def body = new ClientInResource(
                assigner: assigner.id,
                curators: [curator1.id, curator2.id],
                firstName: "First name",
                secondName: "Second name",
                thirdName: "Third name",
                company: "Company",
                birthday: "1990-05-22",
                phoneNumbers: [new ClientPhoneNumberResource("+7", "234", "123132", PhoneNumberType.WORK.name())],
                emails: [new ClientEmailResource("Test@test.com", EmailType.HOME.name())],
                sites: [
                    new ClientSiteResource("work.site.com", SiteType.WORK.name()),
                    new ClientSiteResource("home.site.com", SiteType.HOME.name())
                ],
                messengers: [
                    new ClientMessengerResource("misterX", MessengerType.TELEGRAM.name()),
                    new ClientMessengerResource("misterX", MessengerType.SKYPE.name())
                ]
            )
        
        when:
            def response = client.post()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(body)
                .url("/client/contact")
                .exchange()
        then:
            response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
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
    
    def "should get all available contacts for org successfully"() {
        
        given:
            def user = userTestHelper.newUser("User2", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
            def contact1 = clientTestHelper.newSimpleClient(user)
            def contact2 = clientTestHelper.newSimpleClient(user)
            def contact3 = clientTestHelper.newSimpleClient(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contacts")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$').value(hasSize(3))
                    .jsonPath('$[*].id').value(containsInAnyOrder(
                        (int) contact1.id,
                        (int) contact2.id,
                        (int) contact3.id))
                    .jsonPath('$[*].firstName').value(containsInAnyOrder(
                        contact1.firstName,
                        contact2.firstName,
                        contact3.firstName))
    }
    
    def "should load a created contact successfully"() {
        
        given:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
            def contact = clientTestHelper.newSimpleClient(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contact/" + contact.getId())
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.id').isNotEmpty()
                    .jsonPath('$.firstName').isEqualTo(contact.firstName)
                    .jsonPath('$.secondName').isEqualTo(contact.secondName)
                    .jsonPath('$.thirdName').isEqualTo(contact.thirdName)
    }
    
    def "should failed when id is not presented"() {
        
        given:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contact/invallid")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(ID_NOT_PRESENTED, ID_NOT_PRESENTED_MESSAGE)
    }
    
    def "should failed creating an invalid contact"() {
        
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def assigner = userTestHelper.newUser("Assigner1", org)
            def token = unsafeTokenService.generateNewToken(user)
            def body = new ClientInResource()
        
        when:
            def response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_ASSIGNER_NOT_PRESENTED, CLIENT_ASSIGNER_NOT_PRESENTED_MESSAGE)
        when:
            body.setAssigner(assigner.id)
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_FIRST_NAME_INVALID_LENGTH, CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE)
        when:
            body.setFirstName(StringUtils.generate(1000))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_FIRST_NAME_INVALID_LENGTH, CLIENT_FIRST_NAME_INVALID_LENGTH_MESSAGE)
        when:
            body.setFirstName("First Name")
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_SECOND_NAME_INVALID_LENGTH, CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE)
        when:
            body.setSecondName(StringUtils.generate(1000))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_SECOND_NAME_INVALID_LENGTH, CLIENT_SECOND_NAME_INVALID_LENGTH_MESSAGE)
        when:
            body.setSecondName("Second name")
            body.setThirdName(StringUtils.generate(1000))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_THIRD_NAME_TOO_LONG, CLIENT_THIRD_NAME_TOO_LONG_MESSAGE)
        when:
            body.setThirdName(null)
            body.setCompany(StringUtils.generate(1000))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_COMPANY_TOO_LONG, CLIENT_COMPANY_TOO_LONG_MESSAGE)
        when:
            body.setCompany(null)
            body.setBirthday("invaliddate")
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_BIRTHDAY_INVALID, CLIENT_BIRTHDAY_INVALID_MESSAGE)
        when:
            body.setBirthday("1700-05-10")
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_BIRTHDAY_INVALID, CLIENT_BIRTHDAY_INVALID_MESSAGE)
        when:
            body.setBirthday("2500-05-10")
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_BIRTHDAY_INVALID, CLIENT_BIRTHDAY_INVALID_MESSAGE)
        when:
            body.setBirthday("1950-04-22")
            body.setPhoneNumbers(new ClientPhoneNumberResource(
                countryCode: "+7",
                regionCode: "234",
                phoneNumber:  "123132",
                type: "invalid"
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_PHONE_NUMBER_INVALID, CLIENT_PHONE_NUMBER_INVALID_MESSAGE)
        when:
            body.setPhoneNumbers(new ClientPhoneNumberResource(
                countryCode: "+7",
                regionCode: StringUtils.generate(100),
                phoneNumber:  "123132",
                type: PhoneNumberType.WORK.name()
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_PHONE_NUMBER_INVALID, CLIENT_PHONE_NUMBER_INVALID_MESSAGE)
        when:
            body.setPhoneNumbers(null)
            body.setEmails(new ClientEmailResource(
                email: "Test@test.com",
                type: "invalid"
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_EMAIL_INVALID, CLIENT_EMAIL_INVALID_MESSAGE)
        when:
            body.setEmails(new ClientEmailResource(
                email: "invalid",
                type: EmailType.HOME.name()
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_EMAIL_INVALID, CLIENT_EMAIL_INVALID_MESSAGE)
        when:
            body.setEmails(new ClientEmailResource(
                email: StringUtils.generate(1000),
                type: EmailType.HOME.name()
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_EMAIL_INVALID, CLIENT_EMAIL_INVALID_MESSAGE)
        when:
            body.setEmails(null)
            body.setSites(new ClientSiteResource(
                url: "work.site.com",
                type: "invalid"
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_SITE_INVALID, CLIENT_SITE_INVALID_MESSAGE)
        when:
            body.setSites(new ClientSiteResource(
                url: StringUtils.generate(1000),
                type: SiteType.WORK.name()
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_SITE_INVALID, CLIENT_SITE_INVALID_MESSAGE)
        when:
            body.setSites(null)
            body.setMessengers(new ClientMessengerResource(
                login: "misterX",
                type: "invalid"
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_MESSENGER_INVALID, CLIENT_MESSENGER_INVALID_MESSAGE)
        when:
            body.setMessengers(new ClientMessengerResource(
                login: StringUtils.generate(1000),
                type: MessengerType.TELEGRAM.name()
            ))
            response = sendCreateRequest(token, body)
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(CLIENT_MESSENGER_INVALID, CLIENT_MESSENGER_INVALID_MESSAGE)
    }
    
    private WebTestClient.ResponseSpec sendCreateRequest(String token, ClientInResource body) {
        client.post()
            .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
            .body(body)
            .url("/client/contact")
            .exchange()
    }
    
    def "should load a page of contacts successfully"() {
        
        given:
          
            def firstOrgContactsCount = 20
            def secondOrgContactsCount = 5
    
            def firstOrg = userTestHelper.newOrg()
            def firstUser = userTestHelper.newUser("User1", firstOrg)
            def secondOrg = userTestHelper.newOrg()
            def secondUser = userTestHelper.newUser("User2", secondOrg)
    
            firstOrgContactsCount.times {
                clientTestHelper.newSimpleClient(firstUser)
            }
    
            secondOrgContactsCount.times {
                clientTestHelper.newSimpleClient(secondUser)
            }
    
            def token = unsafeTokenService.generateNewToken(firstUser)
        
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contacts/page?pageSize=5&offset=0")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(20)
                    .jsonPath('$.resources').value(hasSize(5))
        when:
            response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contacts/page?pageSize=10&offset=12")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.totalSize').isEqualTo(20)
                    .jsonPath('$.resources').value(hasSize(8))
    }
    
    def "should update contact successfully"() {
        
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def assigner = userTestHelper.newUser("Assigner1", org)
            def curator1 = userTestHelper.newUser("Curator1", org)
            def curator2 = userTestHelper.newUser("Curator2", org)
            def contact = clientTestHelper.newSimpleClient(user)
    
            def token = unsafeTokenService.generateNewToken(user)
            def body = new ClientInResource(
                id: contact.id,
                assigner: assigner.id,
                curators: [curator1.id, curator2.id],
                firstName: "First name",
                secondName: "Second name",
                thirdName: "Third name",
                company: "Company",
                birthday: "1990-05-22",
                version: contact.version,
                phoneNumbers: [new ClientPhoneNumberResource("+7", "234", "123132", PhoneNumberType.WORK.name())],
                emails: [new ClientEmailResource("Test@test.com", EmailType.HOME.name())],
                sites: [
                    new ClientSiteResource("work.site.com", SiteType.WORK.name()),
                    new ClientSiteResource("home.site.com", SiteType.HOME.name())
                ],
                messengers: [
                    new ClientMessengerResource("misterX", MessengerType.TELEGRAM.name()),
                    new ClientMessengerResource("misterX", MessengerType.SKYPE.name())
                ]
            )
        
        when:
            def response = client.put()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(body)
                .url("/client/contact")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
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
    
    def "should failed updating a contact"() {
        
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def assigner = userTestHelper.newUser("Assigner1", org)
            def contact = clientTestHelper.newSimpleClient(user)
            
            def token = unsafeTokenService.generateNewToken(user)
            def body = new ClientInResource(
                id: contact.id,
                assigner: assigner.id,
                firstName: "First name",
                secondName: "Second name",
                thirdName: "Third name",
                version: 10
            )
        
        when:
            def response = client.put()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(body)
                .url("/client/contact")
                .exchange()
        then:
            response.expectStatus().isEqualTo(HttpStatus.CONFLICT)
        when:
    
            body = new ClientInResource(
                id: contact.id,
                assigner: (assigner.id + 100),
                firstName: "First name",
                secondName: "Second name",
                thirdName: "Third name",
                version: contact.version
            )
        
            response = client.put()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(body)
                .url("/client/contact")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(INVALID_ASSIGNER, INVALID_ASSIGNER_MESSAGE)
    }
}
