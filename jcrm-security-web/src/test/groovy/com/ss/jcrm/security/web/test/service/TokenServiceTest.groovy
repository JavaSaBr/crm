package com.ss.jcrm.security.web.test.service

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException
import com.ss.jcrm.security.web.exception.InvalidTokenException
import com.ss.jcrm.security.web.exception.TokenExpiredException
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.test.WebSecuritySpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.ZonedDateTime
import java.util.concurrent.CompletionException

class TokenServiceTest extends WebSecuritySpecification {

    @Autowired
    UnsafeTokenService unsafeTokenService

    def "should generate a new token for a user"() {

        given:
            def user1 = userTestHelper.newDaoUser("User1")
            def user2 = userTestHelper.newDaoUser("User2")
        when:
            def token1 = unsafeTokenService.generateNewToken(user1)
            def token2 = unsafeTokenService.generateNewToken(user2)
        then:
            token1 != null
            token2 != null
            token1 != token2
    }

    def "should find a user"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            def foundUser = unsafeTokenService.findUserIfNotExpired(token)
        then:
            foundUser != null
            foundUser.getId() == user.getId()
    }

    def "should thrown ObjectNotFoundDaoException when user isn't exist"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            userTestHelper.clearAllData()
            unsafeTokenService.findUserIfNotExpired(token)
        then:
            thrown ObjectNotFoundDaoException
    }

    def "should thrown InvalidTokenException when a token isn't valid"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            unsafeTokenService.findUserIfNotExpired(token + "wefwefwe")
        then:
            thrown InvalidTokenException
    }

    def "should thrown TokenExpiredException when a token is expired"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user, ZonedDateTime.now().minusDays(300))
        when:
            unsafeTokenService.findUserIfNotExpired(token)
        then:
            thrown TokenExpiredException
    }

    def "should thrown InvalidTokenException when a token isn't valid async"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            unsafeTokenService.findUserIfNotExpired(token + "wefwefwe")
        then:
            thrown InvalidTokenException
    }

    def "should thrown TokenExpiredException when a token is expired async"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user, ZonedDateTime.now().minusDays(300))
        when:
            unsafeTokenService.findUserIfNotExpiredAsync(token).join()
        then:
            thrown TokenExpiredException
    }

    def "should thrown ExecutionException -> ObjectNotFoundDaoException when a token is expired async"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = unsafeTokenService.generateNewToken(user)
        when:
            userTestHelper.clearAllData()
            unsafeTokenService.findUserIfNotExpiredAsync(token).join()
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof ObjectNotFoundDaoException
    }
}
