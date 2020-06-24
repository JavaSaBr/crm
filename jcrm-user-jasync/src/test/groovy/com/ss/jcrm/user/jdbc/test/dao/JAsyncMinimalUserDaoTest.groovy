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
            def user = userDao.create("User1", hash, salt, org, roles, null, null, null, null).block()
        then:
            user != null
            user.getEmail() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles().size() == 1
        when:
            def loaded = minimalUserDao.findById(user.getId()).block()
        then:
            loaded != null
            loaded.getEmail() == "User1"
            loaded.getOrganizationId() == org.getId()
            loaded.getRoles().size() == 1
    }
}
