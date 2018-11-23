package com.ss.jcrm.security.web.test.service

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.security.web.test.WebSecuritySpecification
import com.ss.jcrm.security.web.service.TokenService
import com.ss.jcrm.security.web.exception.InvalidTokenException
import com.ss.jcrm.security.web.exception.TokenExpiredException
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.ZonedDateTime

class TokenServiceTest extends WebSecuritySpecification {

    @Autowired
    TokenService tokenService

    def "should generate a new token for a user"() {

        given:
            def user1 = userTestHelper.newDaoUser("User1")
            def user2 = userTestHelper.newDaoUser("User2")
        when:
            def token1 = tokenService.generateNewToken(user1)
            def token2 = tokenService.generateNewToken(user2)
        then:
            token1 != null
            token2 != null
            token1 != token2
    }

    def "should find a user"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = tokenService.generateNewToken(user)
        when:
            def foundUser = tokenService.findUserIfNotExpired(token)
        then:
            foundUser != null
            foundUser.getId() == user.getId()
    }

    def "should thrown ObjectNotFoundDaoException when user isn't exist"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = tokenService.generateNewToken(user)
        when:
            userTestHelper.clearAllData()
            tokenService.findUserIfNotExpired(token)
        then:
            thrown ObjectNotFoundDaoException
    }

    def "should thrown InvalidTokenException when a token isn't valid"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = tokenService.generateNewToken(user)
        when:
            tokenService.findUserIfNotExpired(token + "wefwefwe")
        then:
            thrown InvalidTokenException
    }

    def "should thrown TokenExpiredException when a token is expired"() {

        given:
            def user = userTestHelper.newDaoUser("User1")
            def token = tokenService.generateNewToken(user, ZonedDateTime.now().minusDays(300))
        when:
            tokenService.findUserIfNotExpired(token)
        then:
            thrown TokenExpiredException
    }
}
