package com.ss.jcrm.security.web.test.service

import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import com.ss.jcrm.security.web.test.WebSecuritySpecification

import static com.ss.jcrm.security.web.exception.SecurityErrors.HAS_NO_REQUIRED_ACCESS_ROLE
import static com.ss.jcrm.security.web.exception.SecurityErrors.HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN_MESSAGE

class WebSecurityServiceTest extends WebSecuritySpecification {
    
    def "authorized endpoint test"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .url("/web/security/test/authorized")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
        when:
            response = client.get()
                .url("/web/security/test/authorized")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)
    }
    
    def "authorized endpoint with required curator role test"() {
        
        when:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN, AccessRole.CURATOR)
            def token = unsafeTokenService.generateNewToken(user)
            def response = client.get()
                .url("/web/security/test/required/access/role/curator")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
        when:
            user = userTestHelper.newUser("User2", AccessRole.CURATOR)
            token = unsafeTokenService.generateNewToken(user)
            response = client.get()
                .url("/web/security/test/required/access/role/curator")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
        when:
            user = userTestHelper.newUser("User3", AccessRole.ORG_ADMIN)
            token = unsafeTokenService.generateNewToken(user)
            response = client.get()
                .url("/web/security/test/required/access/role/curator")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isForbidden()
                .verifyErrorResponse(HAS_NO_REQUIRED_ACCESS_ROLE, HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE)
    }
    
    def "authorized endpoint with required curator or org admin role test"() {
    
        when:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN, AccessRole.CURATOR)
            def token = unsafeTokenService.generateNewToken(user)
            def response = client.get()
                .url("/web/security/test/required/access/role/curator/or/org_admin")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
        when:
            user = userTestHelper.newUser("User2", AccessRole.CURATOR)
            token = unsafeTokenService.generateNewToken(user)
            response = client.get()
                .url("/web/security/test/required/access/role/curator/or/org_admin")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
        when:
            user = userTestHelper.newUser("User3", AccessRole.ORG_ADMIN)
            token = unsafeTokenService.generateNewToken(user)
            response = client.get()
                .url("/web/security/test/required/access/role/curator/or/org_admin")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
        when:
            user = userTestHelper.newUser("User4", AccessRole.SUPER_ADMIN)
            token = unsafeTokenService.generateNewToken(user)
            response = client.get()
                .url("/web/security/test/required/access/role/curator/or/org_admin")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isForbidden()
                .verifyErrorResponse(HAS_NO_REQUIRED_ACCESS_ROLE, HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE)
    }
}
