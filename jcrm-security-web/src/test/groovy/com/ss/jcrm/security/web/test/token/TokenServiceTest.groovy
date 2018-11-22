package com.ss.jcrm.security.web.test.token

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException
import com.ss.jcrm.security.Passwords
import com.ss.jcrm.security.web.test.WebSecuritySpecification
import com.ss.jcrm.security.web.token.TokenService
import com.ss.jcrm.security.web.token.exception.InvalidTokenException
import com.ss.jcrm.security.web.token.exception.TokenExpiredException
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.ZonedDateTime

class TokenServiceTest extends WebSecuritySpecification {

    @Autowired
    UserDao userDao

    @Autowired
    OrganizationDao organizationDao

    @Autowired
    TokenService tokenService

    def "should generate a new token for a user"() {

        given:
            def password = Passwords.nextBytePassword(24)
            def salt = Passwords.nextSalt
            def org = organizationDao.create("Org1")
            def user1 = userDao.create("User1", password, salt, org)
            def user2 = userDao.create("User2", password, salt, org)
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
            def password = Passwords.nextBytePassword(24)
            def salt = Passwords.nextSalt
            def org = organizationDao.create("Org")
            def user = userDao.create("User", password, salt, org)
            def token = tokenService.generateNewToken(user)
        when:
            def foundUser = tokenService.findUserIfNotExpired(token)
        then:
            foundUser != null
            foundUser.getId() == user.getId()
    }

    def "should thrown ObjectNotFoundDaoException when user isn't exist"() {

        given:
            def password = Passwords.nextBytePassword(24)
            def salt = Passwords.nextSalt
            def org = organizationDao.create("Org")
            def user = userDao.create("User", password, salt, org)
            def token = tokenService.generateNewToken(user)
        when:
            JdbcUserSpecification.clearAllTables(userDataSource)
            tokenService.findUserIfNotExpired(token)
        then:
            thrown ObjectNotFoundDaoException
    }

    def "should thrown InvalidTokenException when a token isn't valid"() {

        given:
            def password = Passwords.nextBytePassword(24)
            def salt = Passwords.nextSalt
            def org = organizationDao.create("Org")
            def user = userDao.create("User", password, salt, org)
            def token = tokenService.generateNewToken(user)
        when:
            tokenService.findUserIfNotExpired(token + "wefwefwe")
        then:
            thrown InvalidTokenException
    }

    def "should thrown TokenExpiredException when a token is expired"() {

        given:
            def password = Passwords.nextBytePassword(24)
            def salt = Passwords.nextSalt
            def org = organizationDao.create("Org")
            def user = userDao.create("User", password, salt, org)
            def token = tokenService.generateNewToken(user, ZonedDateTime.now().minusDays(300))
        when:
            tokenService.findUserIfNotExpired(token)
        then:
            thrown TokenExpiredException
    }
}
