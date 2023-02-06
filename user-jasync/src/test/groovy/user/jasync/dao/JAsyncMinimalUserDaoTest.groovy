package user.jasync.dao

import com.ss.jcrm.security.service.PasswordService
import crm.user.api.dao.MinimalUserDao
import crm.user.api.dao.OrganizationDao
import crm.user.api.dao.UserDao
import crm.user.api.dao.UserGroupDao
import user.jasync.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

class JAsyncMinimalUserDaoTest extends JAsyncUserSpecification {
  
  @Autowired
  UserGroupDao userRoleDao
  
  @Autowired
  UserDao userDao
  
  @Autowired
  OrganizationDao organizationDao
  
  @Autowired
  PasswordService passwordService
  
  @Autowired
  MinimalUserDao minimalUserDao
  
  def "should load new minimal user"() {
    given:
        def organization = userTestHelper.newOrg()
        def salt = passwordService.nextSalt
        def password = passwordService.nextPassword(24)
        def hash = passwordService.hash(password, salt)
        def roles = userTestHelper.onlyOrgAdminRole()
    when:
        def user = userDao.create("User1", hash, salt, organization, roles).block()
    then:
        user != null
        user.email() == "User1"
        Arrays.equals(user.salt(), salt)
        user.id() != 0L
        user.organization() == organization
        user.roles().size() == 1
    when:
        def loaded = minimalUserDao.findById(user.id()).block()
    then:
        loaded != null
        loaded.email() == "User1"
        loaded.organizationId() == organization.id()
        loaded.roles().size() == 1
  }
  
  def "should load page of minimal users which are in target user group"() {
    given:
        def organization = userTestHelper.newOrg()
        def group1 = userTestHelper.newGroup(organization)
        def group2 = userTestHelper.newGroup(organization)
        def user1 = userTestHelper.newUser("User1", organization)
        def user2 = userTestHelper.newUser("User2", organization)
        def user3 = userTestHelper.newUser("User3", organization)
        def user4 = userTestHelper.newUser("User4", organization)
    when:
        def page = minimalUserDao.findPageByOrganizationAndGroup(0, 10, organization.id(), group1.id()).block()
    then:
        page.entities().isEmpty()
        page.totalSize() == 0
    when:
        
        user1.addGroup(group1)
        user2.addGroup(group1)
        user3.addGroup(group2)
        user4.addGroup(group1)
        
        userDao.update(user1).block()
        userDao.update(user2).block()
        userDao.update(user3).block()
        userDao.update(user4).block()
        
        page = minimalUserDao.findPageByOrganizationAndGroup(0, 10, organization.id(), group1.id()).block()
    then:
        page.totalSize() == 3
        page.entities().containsAll(List.of(
            minimalUserDao.requireById(user1.id()).block(),
            minimalUserDao.requireById(user2.id()).block(),
            minimalUserDao.requireById(user4.id()).block()))
    when:
        page = minimalUserDao.findPageByOrganizationAndGroup(0, 10, organization.id(), group2.id()).block()
    then:
        page.totalSize() == 1
        page.entities().containsAll(List.of(
            minimalUserDao.requireById(user3.id()).block()))
  }
}
