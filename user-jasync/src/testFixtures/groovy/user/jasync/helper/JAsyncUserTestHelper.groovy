package user.jasync.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.db.jasync.JAsyncTestHelper
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import crm.user.api.EmailConfirmation
import crm.user.api.Organization
import crm.user.api.User
import crm.user.api.UserGroup
import crm.user.api.dao.EmailConfirmationDao
import crm.user.api.dao.OrganizationDao
import crm.user.api.dao.UserDao
import crm.user.api.dao.UserGroupDao
import crm.user.api.test.UserTestHelper
import com.ss.jcrm.user.contact.api.Messenger
import com.ss.jcrm.user.contact.api.PhoneNumber
import user.jasync.JAsyncUserSpecification

import java.time.Instant
import java.util.concurrent.ThreadLocalRandom

import static com.ss.jcrm.integration.test.TestUtils.waitForResult

class JAsyncUserTestHelper extends JAsyncTestHelper implements UserTestHelper {
  
  private final UserDao userDao
  private final UserGroupDao userGroupDao
  private final OrganizationDao organizationDao
  private final PasswordService passwordService
  private final DictionaryTestHelper dictionaryTestHelper
  private final EmailConfirmationDao emailConfirmationDao
  
  JAsyncUserTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema,
      UserDao userDao,
      UserGroupDao userGroupDao,
      OrganizationDao organizationDao,
      PasswordService passwordService,
      DictionaryTestHelper dictionaryTestHelper,
      EmailConfirmationDao emailConfirmationDao) {
    super(connectionPool, schema)
    this.userDao = userDao
    this.userGroupDao = userGroupDao
    this.organizationDao = organizationDao
    this.passwordService = passwordService
    this.dictionaryTestHelper = dictionaryTestHelper
    this.emailConfirmationDao = emailConfirmationDao
  }
  
  def synchronized getOrCreateDefaultOrg() {
    
    def organization = waitForResult(organizationDao.findByName("TestOrg"))
    
    if (organization == null) {
      return waitForResult(organizationDao.create("TestOrg", dictionaryTestHelper.newCountry()))
    }
    
    return organization
  }
  
  @Override
  Set<AccessRole> onlyOrgAdminRole() {
    return Set.of(AccessRole.ORG_ADMIN)
  }
  
  @Override
  Organization newOrg() {
    def country = dictionaryTestHelper.newCountry()
    return waitForResult(organizationDao.create(nextUId(), country))
  }
  
  def newOrg(String name) {
    return waitForResult(organizationDao.create(name, dictionaryTestHelper.newCountry()))
  }
  
  @Override
  UserGroup newGroup(Organization organization) {
    return waitForResult(userGroupDao.create(nextUId(), Set.of(), organization))
  }
  
  @Override
  UserGroup newGroup(String name, Organization organization) {
    return waitForResult(userGroupDao.create(name, Set.of(), organization))
  }
  
  @Override
  UserGroup newGroup(String name, Organization organization, Set<AccessRole> roles) {
    return waitForResult(userGroupDao.create(name, roles, organization))
  }
  
  @Override
  User newUser() {
    return newUser(
        nextEmail(),
        passwordService.nextPassword(24),
        passwordService.nextSalt,
        getOrCreateDefaultOrg())
  }
  
  @Override
  User newUser(String name) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.nextSalt,
        getOrCreateDefaultOrg())
  }
  
  @Override
  User newUser(String name, String password) {
    return newUser(
        name,
        password,
        passwordService.nextSalt,
        getOrCreateDefaultOrg())
  }
  
  @Override
  User newUser(String name, AccessRole... roles) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.nextSalt,
        Set.of(roles),
        getOrCreateDefaultOrg())
  }
  
  @Override
  User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.nextSalt,
        getOrCreateDefaultOrg(),
        phoneNumbers,
        messengers)
  }
  
  User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers, String password) {
    return newUser(
        name,
        password,
        passwordService.nextSalt,
        getOrCreateDefaultOrg(),
        phoneNumbers,
        messengers)
  }
  
  @Override
  User newUser(String name, Organization organization) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.nextSalt,
        organization)
  }
  
  @Override
  User newUser(String name, Organization organization, AccessRole... roles) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.nextSalt,
        Set.of(roles),
        organization)
  }
  
  @Override
  User newUser(String email, String firstName, String secondName, String thirdName, Organization organization) {
    def password = passwordService.nextPassword(24)
    def salt = passwordService.nextSalt
    return waitForResult(userDao.create(
        email,
        passwordService.hash(password, salt),
        salt,
        organization,
        onlyOrgAdminRole(),
        firstName,
        secondName,
        thirdName,
        null,
        Collections.emptySet(),
        Collections.emptySet()))
  }
  
  def newUser(String name, String password, byte[] salt, Organization organization) {
    return waitForResult(userDao.create(
        name,
        passwordService.hash(password, salt),
        salt,
        organization,
        Set.of(),
        null,
        null,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()))
  }
  
  def newUser(String name, String password, byte[] salt, Set<AccessRole> accessRoles, Organization organization) {
    return waitForResult(userDao.create(
        name,
        passwordService.hash(password, salt),
        salt,
        organization,
        accessRoles,
        null,
        null,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()))
  }
  
  def newUser(
      String name,
      String password,
      byte[] salt,
      Organization organization,
      Set<PhoneNumber> phoneNumbers,
      Set<Messenger> messengers) {
    return waitForResult(userDao.create(
        name,
        passwordService.hash(password, salt),
        salt,
        organization,
        onlyOrgAdminRole(),
        null,
        null,
        null,
        null,
        phoneNumbers,
        messengers))
  }
  
  def newOrgAdmin() {
    
    def password = passwordService.nextPassword(24);
    def salt = passwordService.nextSalt
    def id = nextUId()
    
    return waitForResult(userDao.create(
        id,
        passwordService.hash(password, salt),
        salt,
        getOrCreateDefaultOrg(),
        onlyOrgAdminRole(),
        null,
        null,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()))
  }
  
  @Override
  void clearAllData() {
    JAsyncUserSpecification.clearAllTables(connectionPool, schema)
  }
  
  @Override
  String nextUId() {
    return System.nanoTime() + "-" + Thread.currentThread().id
  }
  
  def nextCode() {
    
    def code = String.valueOf(System.nanoTime())
    
    if (code.length() > 14) {
      return code.substring(0, 14);
    }
    
    return code
  }
  
  @Override
  String nextEmail() {
    return ThreadLocalRandom.current()
        .nextInt(9) + "-" + (Thread.currentThread().id % 10) + "@tt.co"
  }
  
  @Override
  EmailConfirmation newEmailConfirmation() {
    return waitForResult(emailConfirmationDao.create(
        nextCode(),
        nextEmail(),
        Instant.now() + 60))
  }
}
