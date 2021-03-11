package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.dao.MinimalUserDao
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification
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
            def org = userTestHelper.newOrg()
            def salt = passwordService.nextSalt
            def password = passwordService.nextPassword(24)
            def hash = passwordService.hash(password, salt)
            def roles = userTestHelper.onlyOrgAdminRole()
        when:
            def user = userDao.create("User1", hash, salt, org, roles).block()
        then:
            user != null
            user.email == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.id != 0L
            user.organization == org
            user.roles.size() == 1
        when:
            def loaded = minimalUserDao.findById(user.getId()).block()
        then:
            loaded != null
            loaded.email == "User1"
            loaded.organizationId == org.getId()
            loaded.roles.size() == 1
    }
    
    def "should load page of minimal users which are in target user group"() {
        
        given:
            def org = userTestHelper.newOrg()
            def group1 = userTestHelper.newGroup(org)
            def group2 = userTestHelper.newGroup(org)
            def user1 = userTestHelper.newUser("User1", org)
            def user2 = userTestHelper.newUser("User2", org)
            def user3 = userTestHelper.newUser("User3", org)
            def user4 = userTestHelper.newUser("User4", org)
        when:
            def page = minimalUserDao.findPageByOrgAndGroup(0, 10, org.id, group1.id).block()
        then:
            page.entities.isEmpty()
            page.totalSize == 0
        when:
            
            user1.addGroup(group1)
            user2.addGroup(group1)
            user3.addGroup(group2)
            user4.addGroup(group1)
    
            userDao.update(user1).block()
            userDao.update(user2).block()
            userDao.update(user3).block()
            userDao.update(user4).block()
    
            page = minimalUserDao.findPageByOrgAndGroup(0, 10, org.id, group1.id).block()
        then:
            page.totalSize == 3
            page.entities.containsAll(List.of(
                minimalUserDao.requireById(user1.id).block(),
                minimalUserDao.requireById(user2.id).block(),
                minimalUserDao.requireById(user4.id).block()
            ))
        when:
            page = minimalUserDao.findPageByOrgAndGroup(0, 10, org.id, group2.id).block()
        then:
            page.totalSize == 1
            page.entities.containsAll(List.of(
                minimalUserDao.requireById(user3.id).block()
            ))
    }
}
