package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.user.api.UserRole
import com.ss.jcrm.user.api.dao.UserRoleDao
import com.ss.rlib.common.util.StringUtils
import org.springframework.beans.factory.annotation.Autowired

import javax.sql.DataSource
import java.util.concurrent.CompletableFuture

class JdbcUserRoleDaoTest extends JdbcUserSpecification {

    @Autowired
    UserRoleDao userRoleDao

    @Autowired
    DataSource userDataSource

    def setup() {
        clearTable(userDataSource, TABLE_USER_ROLE)
    }

    def "should create a new role"(String name, String resultName) {

        expect:
            validate(userRoleDao.create(name), resultName)

        where:
            name                   | resultName
            "test1"                | "test1"
            "test5"                | "test5"
            "Test16"               | "Test16"
    }

    def "should create a new role using async"(String name, String resultName) {

        expect:
            validate(userRoleDao.createAsync(name).join(), resultName)

        where:
            name                   | resultName
            "test1"                | "test1"
            "test5"                | "test5"
            "Test16"               | "Test16"
    }

    def "should create and load a new role"() {

        given:
            def roleName = "TestRole1"
            def created = userRoleDao.create(roleName)
        when:
            def roleByName = userRoleDao.findByName(roleName)
            def roleById = userRoleDao.findById(created.getId())
        then:
            roleByName != null
            roleByName.getName() == roleName
            roleByName.getId() != 0L
            roleById != null
            roleById.getId() != 0L
    }

    def "should create and load a new role using async"() {

        given:
            def roleName = "TestOrgName1"
            def created = userRoleDao.createAsync(roleName).join()
        when:
            def roleByName = userRoleDao.findByNameAsync(roleName).join()
            def roleById = userRoleDao.findByIdAsync(created.getId()).join()
        then:
            roleByName != null
            roleByName.getName() == roleName
            roleByName.getId() != 0L
            roleById != null
            roleById.getId() != 0L
    }

    def 'should load all new roles'() {

        given:

            def roleNames = ["role1", "role2", "role3", "role4"]

            roleNames.forEach {
                userRoleDao.create(it)
            }

        when:
            def loaded = userRoleDao.getAll()
        then:
            loaded != null
            loaded.size() == roleNames.size()
    }

    def "should load all new roles using async"() {

        given:

            def roleNames = ["role1", "role2", "role3", "role4"]
            def results = new ArrayList<CompletableFuture<?>>()

            roleNames.forEach {
                results.add(userRoleDao.createAsync(it))
            }

            results.forEach {
                it.join()
            }

        when:
            def loaded = userRoleDao.getAllAsync().join()
        then:
            loaded != null
            loaded.size() == roleNames.size()
    }

    private static boolean validate(UserRole userRole, String resultName) {
        return userRole != null &&
            userRole.getId() != 0 &&
            StringUtils.equals(userRole.getName(), resultName)
    }
}
