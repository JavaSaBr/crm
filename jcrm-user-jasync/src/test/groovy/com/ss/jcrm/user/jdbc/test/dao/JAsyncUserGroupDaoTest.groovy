package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.user.api.UserGroup
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification
import com.ss.rlib.common.util.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Mono

import java.util.concurrent.CompletableFuture

class JAsyncUserGroupDaoTest extends JAsyncUserSpecification {

    @Autowired
    UserGroupDao userGroupDao

    def "should create a new group"(String name) {

        when:
            def created = userGroupDao.create(name, userTestHelper.newOrg()).block()
        then:
            validate(created, name)
        where:
            name << ["group1", "group2", "dweffw", "GROUP3"]
    }

    def "should create and load a new group"() {

        given:
            def org = userTestHelper.newOrg()
            def groupName = "TestGroup1"
            def created = userGroupDao.create(groupName, org).block()
        when:
            def groupById = userGroupDao.findById(created.getId()).block()
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
                userGroupDao.create(it, org).block()
            }

        when:
            def loaded = userGroupDao.findAll(org).block()
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
