package com.ss.jcrm.security.web.test.service

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException
import com.ss.jcrm.security.web.exception.ChangedPasswordTokenException
import com.ss.jcrm.security.web.exception.InvalidTokenException
import com.ss.jcrm.security.web.exception.ExpiredTokenException
import com.ss.jcrm.security.web.exception.MaxRefreshedTokenException
import com.ss.jcrm.security.web.exception.PrematureTokenException
import com.ss.jcrm.security.web.test.WebSecuritySpecification
import org.springframework.beans.factory.annotation.Value

import java.time.ZonedDateTime

class TokenServiceTest extends WebSecuritySpecification {
    
    @Value('${token.max.refreshes}')
    Integer maxRefreshes
    
    def "should generate new token for a user"() {

        given:
            def user1 = userTestHelper.newUser("User1")
            def user2 = userTestHelper.newUser("User2")
        when:
            def token1 = unsafeTokenService.generateNewToken(user1)
            def token2 = unsafeTokenService.generateNewToken(user2)
        then:
            token1 != null
            token2 != null
            token1 != token2
    }

    def "should find user if token is not expired"() {

        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def foundUser = unsafeTokenService.findUserIfNotExpired(token).block()
        then:
            foundUser != null
            foundUser.id() == user.id()
        when:
            token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                0,
                0
            )
            unsafeTokenService.findUserIfNotExpired(token)
        then:
            thrown ExpiredTokenException
    }
    
    def "should find user even if token is expired"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                0,
                0
            )
        when:
            def foundUser = unsafeTokenService.findUser(token).block()
        then:
            foundUser != null
            foundUser.id() == user.id()
    }
    
    def "should refresh token"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def newToken = unsafeTokenService.refresh(user, token)
        then:
            newToken != null
            newToken != token
    }
    
    def "should refresh token after expiration"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                0,
                0
            )
        when:
            def newToken = unsafeTokenService.refresh(user, token)
        then:
            newToken != null
            newToken != token
    }
    
    def "should thrown InvalidTokenException when token isn't valid"() {

        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            unsafeTokenService.findUserIfNotExpired(token + "wefwefwe")
        then:
            thrown InvalidTokenException
    }

    def "should thrown TokenExpiredException when token is expired"() {

        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().minusDays(300),
                ZonedDateTime.now().minusDays(350),
                0,
                0
            )
        when:
            unsafeTokenService.findUserIfNotExpired(token)
        then:
            thrown ExpiredTokenException
    }
    
    def "should thrown PrematureTokenException when token is from future"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(
                user.id,
                ZonedDateTime.now().plusDays(2),
                ZonedDateTime.now().plusDays(1),
                0,
                0
            )
        when:
            unsafeTokenService.findUserIfNotExpired(token)
        then:
            thrown PrematureTokenException
    }
    
    def "should thrown MaxRefreshedTokenException when token has reached max refreshes"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def newToken = unsafeTokenService.refresh(user, token)
            (maxRefreshes + 1).times {
                newToken = unsafeTokenService.refresh(user, newToken)
            }
        then:
            thrown MaxRefreshedTokenException
    }
    
    def "should thrown ChangedPasswordTokenException when user changed password"() {
        
        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
            user.passwordVersion(2)
        when:
            unsafeTokenService.refresh(user, token)
        then:
            thrown ChangedPasswordTokenException
    }

    def "should thrown InvalidTokenException when token isn't valid async"() {

        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            unsafeTokenService.findUserIfNotExpired(token + "wefwefwe")
        then:
            thrown InvalidTokenException
    }

    def "should thrown ObjectNotFoundDaoException when user is not exist"() {

        given:
            def user = userTestHelper.newUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            userTestHelper.clearAllData()
            unsafeTokenService.findUserIfNotExpired(token).block()
        then:
            thrown ObjectNotFoundDaoException
    }

    def "should generate activation code"(int length) {

        when:
            def code = unsafeTokenService.generateActivateCode(length)
        then:
            code != null
            code.length() == length
        where:
            length << [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    }

    def "should throw exception with length less than 1"(int length) {

        when:
            unsafeTokenService.generateActivateCode(length)
        then:
            thrown IllegalArgumentException
        where:
            length << [0, -1, -255, -30]
    }
}
