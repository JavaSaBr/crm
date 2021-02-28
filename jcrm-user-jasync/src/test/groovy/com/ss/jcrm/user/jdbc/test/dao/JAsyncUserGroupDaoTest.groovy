package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.user.api.UserGroup
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

class JAsyncUserGroupDaoTest extends JAsyncUserSpecification {

    @Autowired
    UserGroupDao userGroupDao

    def "should create a new group"(String name, Set<AccessRole> roles) {

        when:
            def created = userGroupDao.create(name, roles, userTestHelper.newOrg()).block()
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

    def "should create and load a new group"() {

        given:
            def org = userTestHelper.newOrg()
            def groupName = "TestGroup1"
            def roles = Set.of(AccessRole.USER_GROUP_MANAGER, AccessRole.DELETE_USER)
            def created = userGroupDao.create(groupName, roles, org).block()
        when:
            def groupById = userGroupDao.findById(created.getId()).block()
        then:
            groupById != null
            groupById.getId() != 0L
            groupById.getName() == groupName
            groupById.getOrganization() == org
    }
    
    def "should throw NotActualObjectDaoException during updating outdated group"() {
        
        given:
            def org = userTestHelper.newOrg()
            def group = userTestHelper.newGroup("Group1", org)
            group.version = -1
            group.setRoles(Set.of(AccessRole.DELETE_USER))
        when:
            userGroupDao.update(group).block()
        then:
            thrown NotActualObjectDaoException
    }
    
    def "should update user group correctly"() {
        
        given:
            def org = userTestHelper.newOrg()
            def group = userTestHelper.newGroup("Group1", org)
            def modified = group.getModified()
        when:
            def loaded = userGroupDao.findByIdAndOrgId(group.id, org.id).block()
        then:
            validate(loaded, "Group1", Set.of())
        when:
            group.setRoles(Set.of(AccessRole.DELETE_USER, AccessRole.CREATE_USER))
            group.setName("Group2")
            def updated = userGroupDao.update(group).block()
        then:
            validate(updated, "Group2", Set.of(AccessRole.DELETE_USER, AccessRole.CREATE_USER))
            updated.modified <=> modified == 1
    }
    
    def "should find groups by names under the same org on russian"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            userTestHelper.newGroup("группа1", org1)
            userTestHelper.newGroup("testGroup", org1)
            userTestHelper.newGroup("тестгруп", org2)
        when:
            def users = userGroupDao.searchByName("груп", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userGroupDao.searchByName("тест", org2.id).block()
        then:
            users.size() == 1
        when:
            users = userGroupDao.searchByName("test", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userGroupDao.searchByName("Gro", org1.id).block()
        then:
            users.size() == 1
        when:
            users = userGroupDao.searchByName("груп", org2.id).block()
        then:
            users.size() == 1
    }
    
    def "should load group only under the same organization"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def group = userTestHelper.newGroup("group1", org1)
        when:
            def loaded = userGroupDao.findByIdAndOrgId(group.id, org1.id).block()
        then:
            loaded != null
            loaded.id != 0L
            loaded.organization == org1
        when:
            loaded = userGroupDao.findByIdAndOrgId(loaded.id, org2.id).block()
        then:
            loaded == null
    }
    
    def "should load groups only under the same organization"() {
        
        given:
            def org1 = userTestHelper.newOrg()
            def org2 = userTestHelper.newOrg()
            def group1 = userTestHelper.newGroup("group1", org1)
            def group2 = userTestHelper.newGroup("group2", org1)
            def group3 = userTestHelper.newGroup("group3", org1)
            def group4 = userTestHelper.newGroup("group4", org2)
            long[] ids = [group1.id, group2.id, group3.id, group4.id]
        when:
            def loaded = userGroupDao.findByIdsAndOrgId(ids, org1.id).block()
        then:
            loaded != null
            loaded.size() == 3
        when:
            loaded = userGroupDao.findByIdsAndOrgId(ids, org2.id).block()
        then:
            loaded.size() == 1
        when:
            ids = [group4.id]
            loaded = userGroupDao.findByIdsAndOrgId(ids, org2.id).block()
        then:
            loaded.size() == 1
    }

    def "should load all groups of some organization"() {

        given:

            def org = userTestHelper.newOrg()
            def groupNames = ["group1", "group2", "group3", "group4"]
            def roles = Set.of(AccessRole.USER_GROUP_MANAGER, AccessRole.DELETE_USER)

            groupNames.forEach {
                userGroupDao.create(it, roles, org).block()
            }

        when:
            def loaded = userGroupDao.findAll(org).block()
        then:
            loaded != null
            loaded.size() == groupNames.size()
    }
    
    def "should load page of user groups"() {
        
        given:
            
            def firstOrgGroupsCount = 20
            def secondOrgGroupsCount = 5
            
            def firstOrg = userTestHelper.newOrg()
            def secondOrg = userTestHelper.newOrg()
            
            def roles = Set.of(AccessRole.CREATE_USER, AccessRole.DELETE_USER)
        
            firstOrgGroupsCount.times {
                userTestHelper.newGroup(userTestHelper.nextUId(), firstOrg, roles)
            }
            
            secondOrgGroupsCount.times {
                userTestHelper.newGroup(userTestHelper.nextUId(), secondOrg, roles)
            }
            
            List<UserGroup> loadedGroups = []
        
        when:
            def page = userGroupDao.findPageByOrg(0, 5, firstOrg.id).block()
            loadedGroups.addAll(page.entities)
        then:
            page != null
            page.totalSize == firstOrgGroupsCount
            page.entities.size() == 5
        when:
            page = userGroupDao.findPageByOrg(17, 5, firstOrg.id).block()
        then:
            page != null
            page.totalSize == firstOrgGroupsCount
            page.entities.size() == 3
            !page.entities.stream().anyMatch({ loadedGroups.contains(it) })
    }
    
    private static boolean validate(UserGroup userGroup, String resultName, Set<AccessRole> roles) {
        return userGroup != null &&
            userGroup.id != 0 &&
            resultName == userGroup.name &&
            roles == userGroup.roles &&
            userGroup.created != null &&
            userGroup.modified != null &&
            userGroup.organization != null
    }
}
