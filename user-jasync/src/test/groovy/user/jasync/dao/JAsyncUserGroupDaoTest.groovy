package user.jasync.dao

import crm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.AccessRole
import crm.user.api.UserGroup
import crm.user.api.dao.UserGroupDao
import crm.user.jasync.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

class JAsyncUserGroupDaoTest extends JAsyncUserSpecification {
  
  @Autowired
  UserGroupDao userGroupDao
  
  def "should create new group"(String name, Set<AccessRole> roles) {
    given:
        def organization = userTestHelper.newOrganization()
    when:
        def created = userGroupDao.create(name, roles, organization).block()
    then:
        validate(created, name, roles)
    where:
        name << ["group1", "group2", "dweffw", "GROUP3"]
        roles << [
            Set.of(),
            Set.of(AccessRole.ORG_ADMIN),
            Set.of(AccessRole.USER_GROUP_MANAGER, AccessRole.DELETE_USER),
            Set.of(AccessRole.CREATE_USER_GROUP)
        ]
  }
  
  def "should create and load new group"() {
    given:
        def organization = userTestHelper.newOrganization()
        def groupName = "TestGroup1"
        def roles = Set.of(AccessRole.USER_GROUP_MANAGER, AccessRole.DELETE_USER)
        def created = userGroupDao.create(groupName, roles, organization).block()
    when:
        def groupById = userGroupDao.findById(created.id()).block()
    then:
        groupById != null
        groupById.id() != 0L
        groupById.name() == groupName
        groupById.organization() == organization
  }
  
  def "should throw NotActualObjectDaoException during updating outdated group"() {
    given:
        def organization = userTestHelper.newOrganization()
        def group = userTestHelper.newGroup("Group1", organization)
        group.version(-1)
        group.roles(Set.of(AccessRole.DELETE_USER))
    when:
        userGroupDao.update(group).block()
    then:
        thrown NotActualObjectDaoException
  }
  
  def "should update user group correctly"() {
    given:
        def organization = userTestHelper.newOrganization()
        def group = userTestHelper.newGroup("Group1", organization)
        def modified = group.modified()
    when:
        def loaded = userGroupDao.findByIdAndOrganization(group.id(), organization.id()).block()
    then:
        validate(loaded, "Group1", Set.of())
    when:
        group.roles(Set.of(AccessRole.DELETE_USER, AccessRole.CREATE_USER))
        group.name("Group2")
        def updated = userGroupDao.update(group).block()
    then:
        validate(updated, "Group2", Set.of(AccessRole.DELETE_USER, AccessRole.CREATE_USER))
        updated.modified() <=> modified == 1
  }
  
  def "should find groups by names under the same org on russian"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        userTestHelper.newGroup("группа1", organization1)
        userTestHelper.newGroup("testGroup", organization1)
        userTestHelper.newGroup("тестгруп", organization2)
    when:
        def users = waitForResults(userGroupDao.searchByName("груп", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userGroupDao.searchByName("тест", organization2.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userGroupDao.searchByName("test", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userGroupDao.searchByName("Gro", organization1.id()))
    then:
        users.size() == 1
    when:
        users = waitForResults(userGroupDao.searchByName("груп", organization2.id()))
    then:
        users.size() == 1
  }
  
  def "should load group only under the same organization"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        def group = userTestHelper.newGroup("group1", organization1)
    when:
        def loaded = userGroupDao.findByIdAndOrganization(group.id(), organization1.id()).block()
    then:
        loaded != null
        loaded.id() != 0L
        loaded.organization() == organization1
    when:
        loaded = userGroupDao.findByIdAndOrganization(loaded.id(), organization2.id()).block()
    then:
        loaded == null
  }
  
  def "should load groups only under the same organization"() {
    given:
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        def group1 = userTestHelper.newGroup("group1", organization1)
        def group2 = userTestHelper.newGroup("group2", organization1)
        def group3 = userTestHelper.newGroup("group3", organization1)
        def group4 = userTestHelper.newGroup("group4", organization2)
        long[] ids = [group1.id(), group2.id(), group3.id(), group4.id()]
    when:
        def loaded = waitForResults(userGroupDao.findByIdsAndOrganization(ids, organization1.id()))
    then:
        loaded != null
        loaded.size() == 3
    when:
        loaded = waitForResults(userGroupDao.findByIdsAndOrganization(ids, organization2.id()))
    then:
        loaded.size() == 1
    when:
        ids = [group4.id()]
        loaded = waitForResults(userGroupDao.findByIdsAndOrganization(ids, organization2.id()))
    then:
        loaded.size() == 1
  }
  
  def "should load all groups of some organization"() {
    given:
        
        def organization = userTestHelper.newOrganization()
        def groupNames = ["group1", "group2", "group3", "group4"]
        def roles = Set.of(AccessRole.USER_GROUP_MANAGER, AccessRole.DELETE_USER)
        
        groupNames.forEach {
          userGroupDao.create(it, roles, organization).block()
        }
    
    when:
        def loaded = waitForResults(userGroupDao.findAll(organization))
    then:
        loaded != null
        loaded.size() == groupNames.size()
  }
  
  def "should load page of user groups"() {
    given:
        
        def firstOrgGroupsCount = 20
        def secondOrgGroupsCount = 5
        
        def organization1 = userTestHelper.newOrganization()
        def organization2 = userTestHelper.newOrganization()
        
        def roles = Set.of(AccessRole.CREATE_USER, AccessRole.DELETE_USER)
        
        firstOrgGroupsCount.times {
          userTestHelper.newGroup(userTestHelper.nextGroupName(), organization1, roles)
        }
        
        secondOrgGroupsCount.times {
          userTestHelper.newGroup(userTestHelper.nextGroupName(), organization2, roles)
        }
        
        List<UserGroup> loadedGroups = []
    
    when:
        def page = userGroupDao.findPageByOrganization(0, 5, organization1.id()).block()
        loadedGroups.addAll(page.entities())
    then:
        page != null
        page.totalSize() == firstOrgGroupsCount
        page.entities().size() == 5
    when:
        page = userGroupDao.findPageByOrganization(17, 5, organization1.id()).block()
    then:
        page != null
        page.totalSize() == firstOrgGroupsCount
        page.entities().size() == 3
        !page.entities().stream().anyMatch({ loadedGroups.contains(it) })
  }
  
  private static boolean validate(UserGroup userGroup, String resultName, Set<AccessRole> roles) {
    return userGroup != null &&
        userGroup.id() != 0 &&
        resultName == userGroup.name() &&
        roles == userGroup.roles() &&
        userGroup.created() != null &&
        userGroup.modified() != null &&
        userGroup.organization() != null
  }
}
