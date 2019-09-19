package com.ss.jcrm.client.web.test.handler

import com.ss.jcrm.client.api.EmailType
import com.ss.jcrm.client.api.MessengerType
import com.ss.jcrm.client.api.PhoneNumberType
import com.ss.jcrm.client.api.SiteType
import com.ss.jcrm.client.web.test.ClientSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import con.ss.jcrm.client.web.resource.ContactEmailResource
import con.ss.jcrm.client.web.resource.ContactInResource
import con.ss.jcrm.client.web.resource.ContactMessengerResource
import con.ss.jcrm.client.web.resource.ContactPhoneNumberResource
import con.ss.jcrm.client.web.resource.ContactSiteResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static com.ss.jcrm.web.exception.CommonErrors.ID_NOT_PRESENTED
import static com.ss.jcrm.web.exception.CommonErrors.ID_NOT_PRESENTED_MESSAGE
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class ContactHandlerTest extends ClientSpecification {
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    def "should create a new contact"() {
    
        given:
            
            def org = userTestHelper.newOrg()
            def user = userTestHelper.newUser("User1", org, AccessRole.ORG_ADMIN)
            def assigner = userTestHelper.newUser("Assigner1", org)
            def curator1 = userTestHelper.newUser("Curator1", org)
            def curator2 = userTestHelper.newUser("Curator2", org)
            
            def token = unsafeTokenService.generateNewToken(user)
            def body = new ContactInResource(
                assignerId: assigner.id,
                curators: [curator1.id, curator2.id],
                firstName: "First name",
                secondName: "Second name",
                thirdName: "Third name",
                company: "Company",
                birthday: "1990-05-22",
                phoneNumbers: [new ContactPhoneNumberResource("+7", "234", "123132", PhoneNumberType.WORK.name())],
                emails: [new ContactEmailResource("Test@test.com", EmailType.HOME.name())],
                sites: [
                    new ContactSiteResource("work.site.com", SiteType.WORK.name()),
                    new ContactSiteResource("home.site.com", SiteType.HOME.name())
                ],
                messengers: [
                    new ContactMessengerResource("misterX", MessengerType.TELEGRAM.name()),
                    new ContactMessengerResource("misterX", MessengerType.SKYPE.name())
                ]
            )
        
        when:
            def response = client.post()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(body)
                .url("/client/contact/create")
                .exchange()
        then:
            response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                    .jsonPath('$.id').isNotEmpty()
                    .jsonPath('$.assigner').isEqualTo((int) body.assignerId)
                    .jsonPath('$.curators[*]').value(containsInAnyOrder(
                        (int) body.curators[0],
                        (int) body.curators[1]
                    ))
                    .jsonPath('$.firstName').isEqualTo(body.firstName)
                    .jsonPath('$.secondName').isEqualTo(body.secondName)
                    .jsonPath('$.thirdName').isEqualTo(body.thirdName)
                    .jsonPath('$.company').isEqualTo(body.company)
                    .jsonPath('$.birthday').isEqualTo(body.birthday)
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
    
    def "should get all available contacts for org"() {
        
        given:
            def user = userTestHelper.newUser("User2", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
            def contact1 = clientTestHelper.newSimpleContact(user)
            def contact2 = clientTestHelper.newSimpleContact(user)
            def contact3 = clientTestHelper.newSimpleContact(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contacts")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
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
    
    def "should load created contact"() {
        
        given:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
            def contact = clientTestHelper.newSimpleContact(user)
        when:
            def response = client.get()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .url("/client/contact/" + contact.getId())
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                    .jsonPath('$.id').isNotEmpty()
                    .jsonPath('$.firstName').isEqualTo(contact.firstName)
                    .jsonPath('$.secondName').isEqualTo(contact.secondName)
                    .jsonPath('$.thirdName').isEqualTo(contact.thirdName)
    }
    
    def "should failed when id not presented"() {
        
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
}
