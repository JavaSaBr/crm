package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.user.api.UserGroup
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import com.ss.rlib.common.util.StringUtils
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletableFuture

class JdbcUserGroupDaoTest extends JdbcUserSpecification {

    @Autowired
    UserGroupDao userGroupDao

    def "should create a new group"(String name) {

        when:
            def created = userGroupDao.create(name, userTestHelper.newOrg())
        then:
            validate(created, name)
        where:
            name << ["group1", "group2", "dweffw", "GROUP3"]
    }

    def "should create a new group using async"(String name) {

        when:
            def created = userGroupDao.createAsync(name, userTestHelper.newOrg()).join()
        then:
            validate(created, name)
        where:
            name << ["group1", "group2", "dweffw", "GROUP3"]
    }

    def "should create and load a new group"() {

        given:
            def org = userTestHelper.newOrg()
            def groupName = "TestGroup1"
            def created = userGroupDao.create(groupName, org)
        when:
            def groupById = userGroupDao.findById(created.getId())
        then:
            groupById != null
            groupById.getId() != 0L
            groupById.getName() == groupName
            groupById.getOrganization() == org
    }

    def "should create and load a new group using async"() {

        given:
            def org = userTestHelper.newOrg()
            def groupName = "TestGroup1"
            def created = userGroupDao.createAsync(groupName, org).join()
        when:
            def groupById = userGroupDao.findByIdAsync(created.getId()).join()
        then:
            groupById != null
            groupById.getId() != 0L
            groupById.getName() == groupName
            groupById.getOrganization() == org
    }

    def "should load all groups of some organization"() {

        given:

            def org = userTestHelper.newOrg()
            def groupNames = ["group1", "group2", "group3", "group4"]

            groupNames.forEach {
                userGroupDao.create(it, org)
            }

        when:
            def loaded = userGroupDao.getAll(org)
        then:
            loaded != null
            loaded.size() == groupNames.size()
    }

    def "should load all groups of some organization using async"() {

        given:

            def org = userTestHelper.newOrg()
            def groupNames = ["role1", "role2", "role3", "role4"]
            def results = new ArrayList<CompletableFuture<?>>()

            groupNames.forEach {
                results.add(userGroupDao.createAsync(it, org))
            }

            results.forEach {
                it.join()
            }

        when:
            def loaded = userGroupDao.getAllAsync(org).join()
        then:
            loaded != null
            loaded.size() == groupNames.size()
    }

    private static boolean validate(UserGroup userGroup, String resultName) {
        return userGroup != null &&
            userGroup.getId() != 0 &&
            StringUtils.equals(userGroup.getName(), resultName) &&
            userGroup.getOrganization() != null
    }
}
