package com.ss.jcrm.client.web.test.handler

import com.ss.jcrm.client.web.test.ClientSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import con.ss.jcrm.client.web.resource.CreateContactInResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class ContactHandlerTest extends ClientSpecification {
    
    @Autowired
    UnsafeTokenService unsafeTokenService
    
    def "should create a new contact"() {
    
        given:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.post()
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .body(new CreateContactInResource("First", "Second", "Third"))
                .url("/client/contact/create")
                .exchange()
        then:
            response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                    .jsonPath('$.id').isNotEmpty()
                    .jsonPath('$.firstName').value(is("First"))
                    .jsonPath('$.secondName').value(is("Second"))
                    .jsonPath('$.thirdName').value(is("Third"))
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
                .jsonPath('$.contacts')
                    .value(hasSize(3))
                .jsonPath('$.contacts[*].id')
                    .value(containsInAnyOrder(
                        (int) contact1.id,
                        (int) contact2.id,
                        (int) contact3.id))
                .jsonPath('$.contacts[*].firstName')
                    .value(containsInAnyOrder(
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
                .jsonPath('$.firstName').value(is(contact.firstName))
                .jsonPath('$.secondName').value(is(contact.secondName))
                .jsonPath('$.thirdName').value(is(contact.thirdName))
    }
}
