package crm.user.jasync.helper;

import static crm.integration.test.util.TestUtils.waitForResult;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.security.AccessRole;
import crm.security.service.PasswordService;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.dictionary.api.DictionaryDbTestHelper;
import crm.integration.test.jasync.JAsyncTestHelper;
import crm.user.api.EmailConfirmation;
import crm.user.api.Organization;
import crm.user.api.User;
import crm.user.api.UserDbTestHelper;
import crm.user.api.UserGroup;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncUserDbTestHelper extends JAsyncTestHelper implements UserDbTestHelper {

  UserDao userDao;
  UserGroupDao userGroupDao;
  OrganizationDao organizationDao;
  PasswordService passwordService;
  DictionaryDbTestHelper dictionaryTestHelper;
  EmailConfirmationDao emailConfirmationDao;

  public JAsyncUserDbTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema,
      UserDao userDao,
      UserGroupDao userGroupDao,
      OrganizationDao organizationDao,
      PasswordService passwordService,
      DictionaryDbTestHelper dictionaryTestHelper,
      EmailConfirmationDao emailConfirmationDao) {
    super(connectionPool, schema);
    this.userDao = userDao;
    this.userGroupDao = userGroupDao;
    this.organizationDao = organizationDao;
    this.passwordService = passwordService;
    this.dictionaryTestHelper = dictionaryTestHelper;
    this.emailConfirmationDao = emailConfirmationDao;
  }

  @Override
  public Set<AccessRole> onlyOrgAdminRole() {
    return Set.of(AccessRole.ORG_ADMIN);
  }

  @Override
  public Organization newOrganization() {
    var country = dictionaryTestHelper.newCountry();
    return waitForResult(organizationDao.create(nextOrganizationName(), country));
  }

  @Override
  public UserGroup newGroup(Organization organization) {
    return waitForResult(userGroupDao.create(nextOrganizationName(), Set.of(), organization));
  }

  @Override
  public UserGroup newGroup(String name, Organization organization) {
    return waitForResult(userGroupDao.create(name, Set.of(), organization));
  }

  @Override
  public UserGroup newGroup(String name, Organization organization, Set<AccessRole> roles) {
    return waitForResult(userGroupDao.create(name, roles, organization));
  }

  @Override
  public User newUser() {
    return newUser(
        nextEmail(),
        passwordService.nextPassword(24),
        passwordService.getNextSalt(),
        getOrCreateDefaultOrg());
  }

  @Override
  public User newUser(String name) {
    return newUser(name, passwordService.nextPassword(24), passwordService.getNextSalt(), getOrCreateDefaultOrg());
  }

  @Override
  public User newUser(String name, String password) {
    return newUser(name, password, passwordService.getNextSalt(), getOrCreateDefaultOrg());
  }

  @Override
  public User newUser(String name, AccessRole... roles) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.getNextSalt(),
        Set.of(roles),
        getOrCreateDefaultOrg());
  }

  @Override
  public User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers) {
    return newUser(
        name,
        passwordService.nextPassword(24),
        passwordService.getNextSalt(),
        getOrCreateDefaultOrg(),
        phoneNumbers,
        messengers);
  }

  public User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers, String password) {
    return newUser(name, password, passwordService.getNextSalt(), getOrCreateDefaultOrg(), phoneNumbers, messengers);
  }

  @Override
  public User newUser(String name, Organization organization) {
    return newUser(name, passwordService.nextPassword(24), passwordService.getNextSalt(), organization);
  }

  @Override
  public User newUser(String name, Organization organization, AccessRole... roles) {
    return newUser(name, passwordService.nextPassword(24), passwordService.getNextSalt(), Set.of(roles), organization);
  }

  @Override
  public User newUser(String email, String firstName, String secondName, String thirdName, Organization organization) {
    var password = passwordService.nextPassword(24);
    var salt = passwordService.getNextSalt();
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
        Collections.emptySet()));
  }

  @Override
  public String nextOrganizationName() {
    return nextId("organization_", "");
  }

  @Override
  public String nextEmail() {
    return nextId("email_", "@it.net");
  }

  @Override
  public String nextGroupName() {
    return nextId("group_", "");
  }

  @Override
  public EmailConfirmation newEmailConfirmation() {
    return waitForResult(emailConfirmationDao.create(
        nextCode(),
        nextEmail(),
        Instant
            .now()
            .plusSeconds(60)));
  }

  private User newUser(String name, String password, byte[] salt, Organization organization) {
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
        Collections.emptySet()));
  }

  private User newUser(
      String name,
      String password,
      byte[] salt,
      Set<AccessRole> accessRoles,
      Organization organization) {
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
        Collections.emptySet()));
  }

  private User newUser(
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
        messengers));
  }

  private User newOrgAdmin() {
    var password = passwordService.nextPassword(24);
    var salt = passwordService.getNextSalt();
    return waitForResult(userDao.create(
        nextOrganizationName(),
        passwordService.hash(password, salt),
        salt,
        getOrCreateDefaultOrg(),
        onlyOrgAdminRole(),
        null,
        null,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()));
  }

  private String nextCode() {

    var code = String.valueOf(System.nanoTime());

    if (code.length() > 14) {
      return code.substring(0, 14);
    }

    return code;
  }

  private synchronized Organization getOrCreateDefaultOrg() {

    var organization = waitForResult(organizationDao.findByName("TestOrg"));

    if (organization == null) {
      return waitForResult(organizationDao.create("TestOrg", dictionaryTestHelper.newCountry()));
    }

    return organization;
  }

}
