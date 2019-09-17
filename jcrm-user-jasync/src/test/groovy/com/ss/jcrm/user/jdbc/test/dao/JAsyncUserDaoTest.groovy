package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.User
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletionException

class JAsyncUserDaoTest extends JAsyncUserSpecification {

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
            def user = userDao.create("User1", hash, salt, org, roles, null, null, null, null).block()
        then:
            user != null
            user.getEmail() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles().size() == 1
        when:
            def loaded = userDao.findById(user.getId()).block()
        then:
            loaded != null
            loaded.getEmail() == "User1"
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

    def "should load a changed user with correct version"() {

        given:
            def user = userTestHelper.newUser("User1")
        when:
            userDao.update(user).block()
        then:
            user.getVersion() == 1
        when:
            def user2 = userDao.findById(user.getId()).block()
        then:
            user2 != null
            user2.getEmail() == user.getEmail()
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
            user.setEmailConfirmed(true)
            user.setPasswordVersion(3)
        when:
            userDao.update(user).block()
        then:
            user.getVersion() == 1
        when:
            def user2 = userDao.findById(user.getId()).block()
        then:
            user2 != null
            user2.getEmail() == user.getEmail()
            user2.getId() == user.getId()
            user2.getFirstName() == user.getFirstName()
            user2.getSecondName() == user.getSecondName()
            user2.getThirdName() == user.getThirdName()
            user2.getPhoneNumber() == user.getPhoneNumber()
            user2.getVersion() == user.getVersion()
            user2.getRoles().size() == 2
            user2.getGroups().size() == 2
            user2.isEmailConfirmed() == user.isEmailConfirmed()
            user2.getPasswordVersion() == 3
    }

    def "should throw NotActualObjectDaoException during changing a user"() {

        given:
            def user = userTestHelper.newUser("User1")
            user.setVersion(-1)
        when:
            userDao.update(user).block()
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof NotActualObjectDaoException
    }

    def "should load a user by name"() {

        given:
            userTestHelper.newUser("User1")
        when:
            def user = userDao.findByEmail("User1").block()
        then:
            user != null
            user.getEmail() == "User1"
            user.getSalt() != null
            user.getId() != 0L
            user.getOrganization() != null
    }
    
    def "should load a user by phone number"() {
    
        given:
            userTestHelper.newUser("User1", "+24223423")
        when:
            def user = userDao.findByPhoneNumber("+24223423").block()
        then:
            user != null
            user.getEmail() == "User1"
            user.getSalt() != null
            user.getId() != 0L
            user.getOrganization() != null
    }

    def "should found created user by name"() {

        given:
            def user = userTestHelper.newUser()
        when:
            def exist = userDao.existByEmail(user.email).block()
        then:
            exist
    }
    
    def "should find users by names under the same org"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            userTestHelper.newUser("user1@mail.com", "FiRst1", "Second1", "Third1", org1)
            userTestHelper.newUser("user2@mail.com", "FIrst2", "Second2", "THird2", org1)
            userTestHelper.newUser("user3@mail.com", "first3", "Second3", "ThIrd3", org1)
            userTestHelper.newUser("user11@mail.com", "First1", "Second1", "Third1", org2)
            userTestHelper.newUser("user12@mail.com", "First2", "Second2", "Third2", org2)
        when:
            def users = userDao.searchByName("user", org1.id).block()
        then:
            users.size() == 3
        when:
            users = userDao.searchByName("First2", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userDao.searchByName("hird", org1.id).block()
        then:
            users.size() == 3
        when:
            users = userDao.searchByName("fir", org1.id).block()
        then:
            users.size() == 3
        when:
            users = userDao.searchByName("@mai", org1.id).block()
        then:
            users.size() == 3
        when:
            users = userDao.searchByName("First1 Seco", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userDao.searchByName("second3 Third", org1.id).block()
        then:
            users.size() == 1
    }
    
    def "should load user only under the same organization"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user = userTestHelper.newUser("user1@mail.com", org1)
        when:
            def loaded = userDao.findByIdAndOrgId(user.id, org1.id).block()
        then:
            loaded != null
            loaded.getEmail() == user.email
            loaded.getId() != 0L
            loaded.getOrganization() == org1
        when:
            loaded = userDao.findByIdAndOrgId(loaded.id, org2.id).block()
        then:
            loaded == null
    }
    
    def "should load users only under the same organization"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def user1 = userTestHelper.newUser("user1@mail.com", org1)
            def user2 = userTestHelper.newUser("user2@mail.com", org1)
            def user3 = userTestHelper.newUser("user3@mail.com", org1)
            def user4 = userTestHelper.newUser("user4@mail.com", org2)
            long[] ids = [user1.id, user2.id, user3.id, user4.id]
        when:
            def loaded = userDao.findByIdsAndOrgId(ids, org1.id).block()
        then:
            loaded != null
            loaded.size() == 3
        when:
            loaded = userDao.findByIdsAndOrgId(ids, org2.id).block()
        then:
            loaded.size() == 1
        when:
            ids = [user4.id]
            loaded = userDao.findByIdsAndOrgId(ids, org2.id).block()
        then:
            loaded.size() == 1
    }
}
