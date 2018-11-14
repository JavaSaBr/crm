package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserRoleDao
import org.springframework.beans.factory.annotation.Autowired

import javax.sql.DataSource
import java.util.concurrent.ThreadLocalRandom

class JdbcUserDaoTest extends JdbcUserSpecification {

    @Autowired
    UserRoleDao userRoleDao

    @Autowired
    UserDao userDao

    @Autowired
    OrganizationDao organizationDao

    @Autowired
    DataSource userDataSource

    def setup() {
        clearTable(userDataSource, TABLE_USER, TABLE_ORGANIZATION, TABLE_USER_ROLE)
    }

    def "should create and load a new user"() {

        given:
            def org = organizationDao.create("TestOrg1")
            def salt = makeSlat(20)
        when:
            def user = userDao.create("User1", "pswd", salt, org)
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
    }

    def "should add two new roles to a user"() {

        given:
            def org = organizationDao.create("TestOrg1")
            def salt = makeSlat(20)
            def user = userDao.create("User1", "pswd", salt, org)
            def firstRole = userRoleDao.create("Role1")
            def secondRole = userRoleDao.create("Role2")
        when:
            user = userDao.addRole(user, firstRole)
            user = userDao.addRole(user, secondRole)
        then:
            user != null
            user.getName() == "User1"
            Arrays.equals(user.getSalt(), salt)
            user.getId() != 0L
            user.getOrganization() == org
            user.getRoles() != null
            user.getRoles().size() == 2
            user.getRoles().contains(firstRole)
            user.getRoles().contains(secondRole)
    }

    def makeSlat(int length) {

        def salt = new byte[length]

        ThreadLocalRandom.current()
            .nextBytes(salt)

        return salt
    }
}
