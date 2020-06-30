package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.User
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.contact.api.Messenger
import com.ss.jcrm.user.contact.api.MessengerType
import com.ss.jcrm.user.contact.api.PhoneNumber
import com.ss.jcrm.user.contact.api.PhoneNumberType
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

class JAsyncUserDaoTest extends JAsyncUserSpecification {

    @Autowired
    UserGroupDao userRoleDao

    @Autowired
    UserDao userDao

    @Autowired
    OrganizationDao organizationDao

    @Autowired
    PasswordService passwordService

    def "should create and load new user"() {

        given:
            def org = userTestHelper.newOrg()
            def salt = passwordService.nextSalt
            def password = passwordService.nextPassword(24)
            def hash = passwordService.hash(password, salt)
            def roles = userTestHelper.onlyOrgAdminRole()
        when:
            def user = userDao.create("User1", hash, salt, org, roles).block()
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

    def "should prevent creating user with the same name"() {

        given:
            userTestHelper.newUser("User1")
        when:
            userTestHelper.newUser("User1")
        then:
            thrown DuplicateObjectDaoException
    }

    def "should load changed user with correct version"() {

        given:
            def user = userTestHelper.newUser("User1")
        when:
            userDao.update(user).block()
        then:
            user.version == 1
        when:
            def user2 = userDao.findById(user.getId()).block()
        then:
            user2 != null
            user2.email == user.email
            user2.id == user.id
            user2.version == user.version
    }

    def "should update user correctly"() {

        given:
            def org = userTestHelper.newOrg()
            def group1 = userTestHelper.newGroup(org)
            def group2 = userTestHelper.newGroup(org)
            def user = userTestHelper.newUser("User1", org)
            def timeAfterCreation = Instant.now()
        when:
            def loaded = userDao.findById(user.id).block()
            def prevModified = loaded.modified
        then:
            loaded != null
            loaded.id == user.id
            loaded.email == "User1"
            loaded.created.isBefore(timeAfterCreation)
            loaded.organization == org
        when:
            Thread.sleep(1000)
            loaded.firstName = "First name"
            loaded.secondName = "Second name"
            loaded.thirdName = "Third name"
            loaded.phoneNumbers = [
                new PhoneNumber("+375", "25", "312333"),
                new PhoneNumber("+375", "25", "123123", PhoneNumberType.MOBILE),
            ]
            loaded.messengers = [
                new Messenger("userTelega", MessengerType.TELEGRAM),
                new Messenger("userSkype", MessengerType.SKYPE)
            ]
            loaded.roles = Set.of(AccessRole.SUPER_ADMIN, AccessRole.ORG_ADMIN)
            loaded.groups = Set.of(group1, group2)
            loaded.emailConfirmed = true
            loaded.passwordVersion = 3
            userDao.update(loaded).block()
            def reloaded = userDao.findById(loaded.id).block()
        then:
            reloaded.id == loaded.id
            reloaded.firstName == loaded.firstName
            reloaded.secondName == loaded.secondName
            reloaded.thirdName == loaded.thirdName
            reloaded.phoneNumbers == loaded.phoneNumbers
            reloaded.messengers == loaded.messengers
            reloaded.modified.isAfter(reloaded.created)
            reloaded.modified.isAfter(prevModified)
            reloaded.version == 1
        when:
            reloaded.thirdName = "Third name 2"
            userDao.update(reloaded).block()
            reloaded = userDao.findById(reloaded.id).block()
        then:
            reloaded.version == 2
    }

    def "should throw NotActualObjectDaoException during upating outdated user"() {

        given:
            def user = userTestHelper.newUser("User1")
            user.version = -1
        when:
            userDao.update(user).block()
        then:
            thrown NotActualObjectDaoException
    }

    def "should load user by email"() {

        given:
            userTestHelper.newUser("User1")
        when:
            def user = userDao.findByEmail("User1").block()
        then:
            user != null
            user.email == "User1"
            user.salt != null
            user.id != 0L
            user.organization != null
    }
    
    def "should load user by phone number"() {
    
        given:
           
            Set<PhoneNumber> phoneNumbers = [
                new PhoneNumber("+24", "22", "3423"),
                new PhoneNumber("+24", "22", "54342")
            ]
    
            Set<Messenger> messengers = [new Messenger("userWatsUp", MessengerType.WHATS_UP)]
        
            userTestHelper.newUser("User1", phoneNumbers, messengers)
        
        when:
            def user = userDao.findByPhoneNumber("+24223423").block()
        then:
            user != null
            user.email == "User1"
            user.salt != null
            user.id != 0L
            user.organization != null
            user.phoneNumbers == phoneNumbers
    }

    def "should found created user by email"() {

        given:
            def user = userTestHelper.newUser()
        when:
            def exist = userDao.existByEmail(user.email).block()
        then:
            exist
    }
    
    def "should find users by names or email under the same org"() {
        
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
    
    def "should find users by names or email under the same org on russian"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            userTestHelper.newUser("user1@mail.com", "АЛекс1", "ФАМил1", "Отчест1", org1)
            userTestHelper.newUser("user2@mail.com", "алеКС2", "Фамил2", "ОТЧЕст2", org1)
            userTestHelper.newUser("user12@mail.com", "Алекс1", "ФаМиЛ1", "ОтЧЕСтВ1", org2)
        when:
            def users = userDao.searchByName("user", org1.id).block()
        then:
            users.size() == 2
        when:
            users = userDao.searchByName("алекс2", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userDao.searchByName("Отчест", org1.id).block()
        then:
            users.size() == 2
        when:
            users = userDao.searchByName("лЕКС", org1.id).block()
        then:
            users.size() == 2
        when:
            users = userDao.searchByName("алекс1 ФАМИЛ", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userDao.searchByName("АЛЕКС2 фам", org1.id).block()
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
    
    def "should load page of users"() {
        
        given:
    
            def firstOrgUsersCount = 20
            def secondOrgUsersCount = 5
    
            def firstOrg = userTestHelper.newOrg()
            def secondOrg = userTestHelper.newOrg()
    
            firstOrgUsersCount.times {
                userTestHelper.newUser(userTestHelper.nextUId(), firstOrg)
            }
    
            secondOrgUsersCount.times {
                userTestHelper.newUser(userTestHelper.nextUId(), secondOrg)
            }
    
            List<User> loadedUsers = []
        
        when:
            def page = userDao.findPageByOrg(0, 5, firstOrg.id).block()
            loadedUsers.addAll(page.entities)
        then:
            page != null
            page.totalSize == firstOrgUsersCount
            page.entities.size() == 5
        when:
            page = userDao.findPageByOrg(17, 5, firstOrg.id).block()
        then:
            page != null
            page.totalSize == firstOrgUsersCount
            page.entities.size() == 3
            !page.entities.stream().anyMatch({ loadedUsers.contains(it) })
    }
}
