package com.ss.jcrm.security.web.test.service

import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.web.service.WebRequestSecurityService
import com.ss.jcrm.security.web.test.WebSecuritySpecification

import static com.ss.jcrm.security.web.exception.SecurityErrors.HAS_NO_REQUIRED_ACCESS_ROLE
import static com.ss.jcrm.security.web.exception.SecurityErrors.HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN
import static com.ss.jcrm.security.web.exception.SecurityErrors.NOT_PRESENTED_TOKEN_MESSAGE

class WebSecurityServiceTest extends WebSecuritySpecification {
    
    def "should successful call security endpoint"() {
        
        given:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN, AccessRole.CURATOR)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .url("/web/security/test/required/access/role/curator")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isOk()
    }
    
    def "should failed without passed token"() {
        when:
            def response = client.get()
                .url("/web/security/test/required/access/role/curator")
                .exchange()
        then:
            response.expectStatus().isUnauthorized()
                .verifyErrorResponse(NOT_PRESENTED_TOKEN, NOT_PRESENTED_TOKEN_MESSAGE)
    }
    
    def "should failed when user doesnt have required access role"() {
        
        given:
            def user = userTestHelper.newUser("User1", AccessRole.ORG_ADMIN)
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def response = client.get()
                .url("/web/security/test/required/access/role/curator")
                .headerValue(WebRequestSecurityService.HEADER_TOKEN, token)
                .exchange()
        then:
            response.expectStatus().isForbidden()
                .verifyErrorResponse(HAS_NO_REQUIRED_ACCESS_ROLE, HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE)
    }
}
