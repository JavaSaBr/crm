package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletionException

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
            def org = userTestHelper.newOrg()
            def salt = passwordService.nextSalt
            def password = passwordService.nextPassword(24)
            def hash = passwordService.hash(password, salt)
            def roles = userTestHelper.onlyOrgAdminRole()
        when:
            def user = userDao.create("User1", hash, salt, org, roles, null, null, null, null)
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles().size() == 1
        when:
            def loaded = userDao.findById(user.getId())
        then:
            loaded != null
            loaded.getName() == "User1"
            Arrays.equals(loaded.getSalt(), salt)
            loaded.getOrganization() == org
            loaded.getRoles().size() == 1
    }

    def "should create and load a new user using async"() {

        given:
            def org = userTestHelper.newOrg()
            def salt = passwordService.nextSalt
            def password = passwordService.nextPassword(24)
            def hash = passwordService.hash(password, salt)
            def roles = userTestHelper.onlyOrgAdminRole()
        when:
            def user = userDao.createAsync("User1", hash, salt, org, roles, null, null, null, null).join()
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles().size() == 1
        when:
            def loaded = userDao.findByIdAsync(user.getId()).join()
        then:
            loaded != null
            loaded.getName() == "User1"
            Arrays.equals(loaded.getSalt(), salt)
            loaded.getOrganization() == org
            loaded.getRoles().size() == 1
    }

    def "should prevent creating a user with the same name"() {

        given:
            userTestHelper.newUser("User1")
        when:
            userTestHelper.newUser("User1")
        then:
            thrown DuplicateObjectDaoException
    }

    def "should prevent creating a user with the same name using async"() {

        given:
            userTestHelper.newUserUsingAsync("User1")
        when:
            userTestHelper.newUserUsingAsync("User1")
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof DuplicateObjectDaoException
    }

    def "should load a changed user with correct version"() {

        given:
            def user = userTestHelper.newUser("User1")
        when:
            userDao.update(user)
        then:
            user.getVersion() == 1
        when:
            def user2 = userDao.findById(user.getId())
        then:
            user2 != null
            user2.getName() == user.getName()
            user2.getId() == user.getId()
            user2.getVersion() == user.getVersion()
    }

    def "should update user correctly"() {

        given:
        def org = userTestHelper.newOrg()
            def group1 = userTestHelper.newGroup(org)
            def group2 = userTestHelper.newGroup(org)
            def user = userTestHelper.newUser("User1", org)
            user.setFirstName("First name")
            user.setSecondName("Second name")
            user.setThirdName("Third name")
            user.setPhoneNumber("Phone number")
            user.setRoles(Set.of(AccessRole.SUPER_ADMIN, AccessRole.ORG_ADMIN))
            user.setGroups(Set.of(group1, group2))
        when:
            userDao.update(user)
        then:
            user.getVersion() == 1
        when:
            def user2 = userDao.findById(user.getId())
        then:
            user2 != null
            user2.getName() == user.getName()
            user2.getId() == user.getId()
            user2.getFirstName() == user.getFirstName()
            user2.getSecondName() == user.getSecondName()
            user2.getThirdName() == user.getThirdName()
            user2.getPhoneNumber() == user.getPhoneNumber()
            user2.getVersion() == user.getVersion()
            user2.getRoles().size() == 2
            user2.getGroups().size() == 2
    }

    def "should throw NotActualObjectDaoException during changing a user"() {

        given:
            def user = userTestHelper.newUser("User1")
            user.setVersion(-1)
        when:
            userDao.update(user)
        then:
            thrown NotActualObjectDaoException
    }

    def "should load a user by name"() {

        given:
            userTestHelper.newUser("User1")
        when:
            def user = userDao.findByName("User1")
        then:
            user != null
            user.getName() == "User1"
            user.getSalt() != null
            user.getId() != 0L
            user.getOrganization() != null
    }
}
