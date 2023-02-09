package user.jasync.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import crm.user.api.User
import crm.user.api.dao.OrganizationDao
import crm.user.api.dao.UserDao
import crm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.contact.api.Messenger
import com.ss.jcrm.user.contact.api.MessengerType
import com.ss.jcrm.user.contact.api.PhoneNumber
import com.ss.jcrm.user.contact.api.PhoneNumberType
import crm.user.jasync.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant
import java.time.LocalDate

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
        
        def organization = userTestHelper.newOrganization()
        def salt = passwordService.nextSalt
        def password = passwordService.nextPassword(24)
        def hash = passwordService.hash(password, salt)
        def roles = userTestHelper.onlyOrgAdminRole()
        def birthday = LocalDate.of(1990, 5, 15)
        def email = userTestHelper.nextEmail()
        
        Set<PhoneNumber> phoneNumbers = [
            new PhoneNumber("+375", "234", "6786786", PhoneNumberType.MOBILE),
            new PhoneNumber("+375", "25", "2344", PhoneNumberType.WORK),
        ]
        
        Set<Messenger> messengers = [
            new Messenger("userTelega", MessengerType.TELEGRAM),
            new Messenger("userSkype", MessengerType.SKYPE)
        ]
    
    when:
        def user = userDao.create(
            email,
            hash,
            salt,
            organization,
            roles,
            "F name",
            "S name",
            "T name",
            birthday,
            phoneNumbers,
            messengers).block()
    then:
        user != null
        user.email() == email
        Arrays.equals(user.salt(), salt)
        user.id() != 0L
        user.organization() == organization
        user.roles().size() == 1
    when:
        def loaded = userDao.findById(user.id()).block()
    then:
        loaded != null
        loaded.email() == email
        Arrays.equals(loaded.salt(), salt)
        loaded.organization() == organization
        loaded.roles().size() == 1
        loaded.firstName() == "F name"
        loaded.secondName() == "S name"
        loaded.thirdName() == "T name"
        loaded.phoneNumbers() == phoneNumbers
        loaded.messengers() == messengers
        loaded.birthday() == birthday
  }
  
  def "should prevent creating user with the same name"() {
    given:
        def email = userTestHelper.nextEmail()
        userTestHelper.newUser(email)
    when:
        userTestHelper.newUser(email)
    then:
        thrown DuplicateObjectDaoException
  }
  
  def "should load changed user with correct version"() {
    given:
        def user = userTestHelper.newUser()
    when:
        userDao.update(user).block()
    then:
        user.version() == 1
    when:
        def user2 = userDao.findById(user.id()).block()
    then:
        user2 != null
        user2.email() == user.email()
        user2.id() == user.id()
        user2.version() == user.version()
  }
  
  def "should update user correctly"() {
    given:
        def organization = userTestHelper.newOrganization()
        def group1 = userTestHelper.newGroup(organization)
        def group2 = userTestHelper.newGroup(organization)
        def user = userTestHelper.newUser(organization)
        def timeAfterCreation = Instant.now()
        def birthday = LocalDate.of(1990, 10, 10)
    when:
        def loaded = userDao.findById(user.id()).block()
        def prevModified = loaded.modified()
    then:
        loaded != null
        loaded.id() == user.id()
        loaded.email() == user.email()
        loaded.created().isBefore(timeAfterCreation)
        loaded.organization() == organization
    when:
        
        Thread.sleep(1000)
        
        loaded.firstName("First name")
        loaded.secondName("Second name")
        loaded.thirdName("Third name")
        loaded.phoneNumbers(Set.of(
            new PhoneNumber("+375", "25", "312333"),
            new PhoneNumber("+375", "25", "123123", PhoneNumberType.MOBILE)
        ))
        loaded.messengers(Set.of(
            new Messenger("userTelega", MessengerType.TELEGRAM),
            new Messenger("userSkype", MessengerType.SKYPE)))
        loaded.roles(Set.of(AccessRole.SUPER_ADMIN, AccessRole.ORG_ADMIN))
        loaded.groups(Set.of(group1, group2))
        loaded.birthday(birthday)
        loaded.emailConfirmed(true)
        loaded.passwordVersion(3)
        
        userDao.update(loaded).block()
        
        def reloaded = userDao.findById(loaded.id()).block()
    
    then:
        reloaded.id() == loaded.id()
        reloaded.firstName() == loaded.firstName()
        reloaded.secondName() == loaded.secondName()
        reloaded.thirdName() == loaded.thirdName()
        reloaded.phoneNumbers() == loaded.phoneNumbers()
        reloaded.messengers() == loaded.messengers()
        reloaded.birthday() == loaded.birthday()
        reloaded.modified().isAfter(reloaded.created())
        reloaded.modified().isAfter(prevModified)
        reloaded.version() == 1
    when:
        reloaded.thirdName("Third name 2")
        userDao.update(reloaded).block()
        reloaded = userDao.findById(reloaded.id()).block()
    then:
        reloaded.version() == 2
  }
  
  def "should throw NotActualObjectDaoException during updating outdated user"() {
    given:
        def user = userTestHelper.newUser()
        user.version(-1)
    when:
        userDao.update(user).block()
    then:
        thrown NotActualObjectDaoException
  }
  
  def "should load user by email"() {
    given:
        def created = userTestHelper.newUser()
    when:
        def user = userDao.findByEmail(created.email()).block()
    then:
        user != null
        user.email() == created.email()
        user.salt() != null
        user.id() != 0L
        user.organization() != null
  }
  
  def "should load user by phone number"() {
    given:
        Set<PhoneNumber> phoneNumbers = [
            new PhoneNumber("+24", "22", "3423"),
            new PhoneNumber("+24", "22", "54342")
        ]
        
        Set<Messenger> messengers = [
            new Messenger("userWatsUp", MessengerType.WHATS_UP)
        ]
        
        def email = userTestHelper.nextEmail()
        
        userTestHelper.newUser(email, phoneNumbers, messengers)
    
    when:
        def user = userDao.findByPhoneNumber("+24223423").block()
    then:
        user != null
        user.email() == email
        user.salt() != null
        user.id() != 0L
        user.organization() != null
        user.phoneNumbers() == phoneNumbers
  }
  
  def "should found created user by email"() {
    given:
        def user = userTestHelper.newUser()
    when:
        def exist = userDao.existByEmail(user.email()).block()
    then:
        exist
  }
  
  def "should find users by names or email under the same org"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        userTestHelper.newUser(userTestHelper.nextEmail(), "FiRst1", "Second1", "Third1", organization1)
        userTestHelper.newUser(userTestHelper.nextEmail(), "FIrst2", "Second2", "THird2", organization1)
        userTestHelper.newUser(userTestHelper.nextEmail(), "first3", "Second3", "ThIrd3", organization1)
        userTestHelper.newUser(userTestHelper.nextEmail(), "First1", "Second1", "Third1", organization2)
        userTestHelper.newUser(userTestHelper.nextEmail(), "First2", "Second2", "Third2", organization2)
    when:
        def users = waitForResults(userDao.searchByName("@it.net", organization1.id()))
    then:
        users.size() == 3
    when:
        users = waitForResults(userDao.searchByName("First2", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userDao.searchByName("hird", organization1.id()))
    then:
        users.size() == 3
    when:
        users = waitForResults(userDao.searchByName("fir", organization1.id()))
    then:
        users.size() == 3
    when:
        users = waitForResults(userDao.searchByName("email_", organization1.id()))
    then:
        users.size() == 3
    when:
        users = waitForResults(userDao.searchByName("First1 Seco", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userDao.searchByName("second3 Third", organization1.id()))
    then:
        users.size() == 1
  }
  
  def "should find users by names or email under the same org on russian"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        
        def firstEmail = userTestHelper.nextEmail()
        def secondEmail = userTestHelper.nextEmail()
        def thirdEmail = userTestHelper.nextEmail()
        
        userTestHelper.newUser(firstEmail, "АЛекс1", "ФАМил1", "Отчест1", organization1)
        userTestHelper.newUser(secondEmail, "алеКС2", "Фамил2", "ОТЧЕст2", organization1)
        userTestHelper.newUser(thirdEmail, "Алекс1", "ФаМиЛ1", "ОтЧЕСтВ1", organization2)
    when:
        def users = waitForResults(userDao.searchByName("email", organization1.id()))
    then:
        users.size() == 2
    when:
        users = waitForResults(userDao.searchByName("алекс2", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userDao.searchByName("Отчест", organization1.id()))
    then:
        users.size() == 2
    when:
        users = waitForResults(userDao.searchByName("лЕКС", organization1.id()))
    then:
        users.size() == 2
    when:
        users = waitForResults(userDao.searchByName("алекс1 ФАМИЛ", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userDao.searchByName("АЛЕКС2 фам", organization1.id()))
    then:
        users.size() == 1
  }
  
  def "should load user only under the same organization"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        def user = userTestHelper.newUser(organization1)
    when:
        def loaded = userDao.findByIdAndOrganization(user.id(), organization1.id()).block()
    then:
        loaded != null
        loaded.email() == user.email()
        loaded.id() != 0L
        loaded.organization() == organization1
    when:
        loaded = userDao.findByIdAndOrganization(loaded.id(), organization2.id()).block()
    then:
        loaded == null
  }
  
  def "should load users only under the same organization"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        def user1 = userTestHelper.newUser(organization1)
        def user2 = userTestHelper.newUser(organization1)
        def user3 = userTestHelper.newUser(organization1)
        def user4 = userTestHelper.newUser(organization2)
        long[] ids = [user1.id(), user2.id(), user3.id(), user4.id()]
    when:
        def loaded = waitForResults(userDao.findByIdsAndOrganization(ids, organization1.id()))
    then:
        loaded != null
        loaded.size() == 3
    when:
        loaded = waitForResults(userDao.findByIdsAndOrganization(ids, organization2.id()))
    then:
        loaded.size() == 1
    when:
        ids = [user4.id()]
        loaded = waitForResults(userDao.findByIdsAndOrganization(ids, organization2.id()))
    then:
        loaded.size() == 1
  }
  
  def "should load page of users"() {
    given:
        
        def firstOrgUsersCount = 20
        def secondOrgUsersCount = 5
        
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        
        firstOrgUsersCount.times {
          userTestHelper.newUser(userTestHelper.nextEmail(), organization1)
        }
        
        secondOrgUsersCount.times {
          userTestHelper.newUser(userTestHelper.nextEmail(), organization2)
        }
        
        List<User> loadedUsers = []
    
    when:
        def page = userDao.findPageByOrganization(0, 5, organization1.id()).block()
        loadedUsers.addAll(page.entities())
    then:
        page != null
        page.totalSize() == firstOrgUsersCount
        page.entities().size() == 5
    when:
        page = userDao.findPageByOrganization(17, 5, organization1.id()).block()
    then:
        page != null
        page.totalSize() == firstOrgUsersCount
        page.entities().size() == 3
        !page.entities().stream().anyMatch({ loadedUsers.contains(it) })
  }
  
  def "should return true when all users are in the same organization"() {
    given:
        
        def organization = userTestHelper.newOrganization()
        def user1 = userTestHelper.newUser(organization)
        def user2 = userTestHelper.newUser(organization)
        def user3 = userTestHelper.newUser(organization)
        
        long[] ids = [user1.id(), user2.id(), user3.id()]
    
    when:
        def result = userDao.containsAll(ids, organization.id()).block()
    then:
        result
  }
  
  def "should return false when all users are in different organizations"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        def user1 = userTestHelper.newUser(organization1)
        def user2 = userTestHelper.newUser(organization1)
        def user3 = userTestHelper.newUser(organization1)
        def user4 = userTestHelper.newUser(organization2)
        long[] ids = [user1.id(), user2.id(), user3.id(), user4.id()]
    when:
        def result = userDao.containsAll(ids, organization1.id()).block()
    then:
        !result
  }
}
