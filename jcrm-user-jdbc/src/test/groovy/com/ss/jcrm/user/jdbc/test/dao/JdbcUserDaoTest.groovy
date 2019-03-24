package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletionException
import java.util.concurrent.ThreadLocalRandom

class JdbcUserDaoTest extends JdbcUserSpecification {

    @Autowired
    UserGroupDao userRoleDao

    @Autowired
    UserDao userDao

    @Autowired
    OrganizationDao organizationDao

    @Autowired
    PasswordService passwordService

    def "should create and load a new user"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
        when:
            def user = userDao.create("User1", password, salt, org)
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
    }

    def "should create and load a new user using async"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
        when:
            def user = userDao.createAsync("User1", password, salt, org).join()
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
    }

    def "should prevent creating a user with the same name"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            userDao.create("User1", password, salt, org)
        when:
            userDao.create("User1", password, salt, org)
        then:
            thrown DuplicateObjectDaoException
    }

    def "should prevent creating a user with the same name using async"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            userDao.createAsync("User1", password, salt, org).join()
        when:
            userDao.createAsync("User1", password, salt, org).join()
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof DuplicateObjectDaoException
    }

    def "should load a changed user with correct version"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            def user = userDao.create("User1", password, salt, org)
            def role = userRoleDao.create("TestRole")
            userDao.addGroup(user, role)
        when:
            def user2 = userDao.findById(user.getId())
        then:
            user2 != null
            user2.getName() == user.getName()
            user2.getId() == user.getId()
            user2.getVersion() == user.getVersion()
    }

    def "should add two new roles to a user"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            def user = userDao.create("User1", password, salt, org)
            def firstRole = userRoleDao.create("Role1")
            def secondRole = userRoleDao.create("Role2")
        when:
            userDao.addGroup(user, firstRole)
            userDao.addGroup(user, secondRole)
        then:
            user != null
            user.getName() == "User1"
            user.getVersion() == 2
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles() != null
            user.getRoles().size() == 2
            user.getRoles().contains(firstRole)
            user.getRoles().contains(secondRole)
    }

    def "should add two new roles to a user using async"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            def user = userDao.create("User1", password, salt, org)
            def firstRole = userRoleDao.create("Role1")
            def secondRole = userRoleDao.create("Role2")
        when:

            userDao.addRoleAsync(user, firstRole)
                .thenAccept { userDao.addGroup(user, secondRole) }
                .join()

        then:
            user != null
            user.getName() == "User1"
            user.getVersion() == 2
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles() != null
            user.getRoles().size() == 2
            user.getRoles().contains(firstRole)
            user.getRoles().contains(secondRole)
    }

    def "should throw NotActualObjectDaoException during changing a user"() {

        given:

            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)

            def password = passwordService.nextBytePassword(24)
            def user = userDao.create("User1", password, salt, org)
            user.setVersion(5)

            def role = userRoleDao.create("Role1")

        when:
            userDao.addGroup(user, role)
        then:
            thrown NotActualObjectDaoException
    }

    def "should throw DuplicateObjectDaoException during creating a new user"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            userDao.create("User1", password, salt, org)
        when:
            userDao.create("User1", password, salt, org)
        then:
            thrown DuplicateObjectDaoException
    }

    def "should remove all roles from a user"() {

        given:
            def org =userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def firstRole = userRoleDao.create("Role1")
            def secondRole = userRoleDao.create("Role2")
            def password = passwordService.nextBytePassword(24)
            def user = userDao.create("User1", password, salt, org)
            userDao.addGroup(user, firstRole)
            userDao.addGroup(user, secondRole)
        when:
            userDao.removeRole(user, firstRole)
        then:
            user != null
            user.getVersion() == 3
            user.getRoles() != null
            user.getRoles().size() == 1
            !user.getRoles().contains(firstRole)
            user.getRoles().contains(secondRole)
        when:
            userDao.removeRole(user, secondRole)
        then:
            user != null
            user.getVersion() == 4
            user.getOrganization() == org
            user.getRoles() != null
            user.getRoles().size() == 0
    }

    def "should load a user by name"() {

        given:
            def org = userTestHelper.newOrg("TestOrg1")
            def salt = makeSlat(20)
            def password = passwordService.nextBytePassword(24)
            userDao.create("User1", password, salt, org)
        when:
            def user = userDao.findByName("User1")
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
    }

    def makeSlat(int length) {

        def salt = new byte[length]

        ThreadLocalRandom.current()
            .nextBytes(salt)

        return salt
    }
}
