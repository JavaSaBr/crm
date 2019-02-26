package com.ss.jcrm.user.jdbc.test.dao

import com.ss.jcrm.user.api.Organization
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import com.ss.rlib.common.util.StringUtils
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletableFuture

class JdbcOrganizationDaoTest extends JdbcUserSpecification {

    @Autowired
    OrganizationDao organizationDao

    def "should create a new organization"(String name, String resultName) {

        expect:
            validate(organizationDao.create(name), resultName)

        where:
            name                   | resultName
            "test1"                | "test1"
            "test5"                | "test5"
            "Test16"               | "Test16"
    }

    def "should create a new organization using async"(String name, String resultName) {

        expect:
            validate(organizationDao.createAsync(name).join(), resultName)

        where:
            name                   | resultName
            "test1"                | "test1"
            "test5"                | "test5"
            "Test16"               | "Test16"
    }

    def "should create and load a new organization"() {

        given:
            def orgName = "TestOrgName1"
            def created = organizationDao.create(orgName)
        when:
            def orgByName = organizationDao.findByName(orgName)
            def orgById = organizationDao.findById(created.getId())
        then:
            orgByName != null
            orgByName.getName() == orgName
            orgByName.getId() != 0L
            orgById != null
            orgById.getId() != 0L
    }

    def "should create and load a new organization using async"() {

        given:
            def orgName = "TestOrgName1"
            def created = organizationDao.createAsync(orgName).join()
        when:
            def orgByName = organizationDao.findByNameAsync(orgName).join()
            def orgById = organizationDao.findByIdAsync(created.getId()).join()
        then:
            orgByName != null
            orgByName.getName() == orgName
            orgByName.getId() != 0L
            orgById != null
            orgById.getId() != 0L
    }

    def "should load all new organizations"() {

        given:

            def orgNames = ["org1", "org2", "org3", "org4"]

            orgNames.forEach {
                organizationDao.create(it)
            }

        when:
            def loaded = organizationDao.getAll()
        then:
            loaded != null
            loaded.size() == orgNames.size()
    }

    def "should load all new organizations using async"() {

        given:

            def orgNames = ["org1", "org2", "org3", "org4"]
            def results = new ArrayList<CompletableFuture<?>>()

            orgNames.forEach {
                results.add(organizationDao.createAsync(it))
            }

            results.forEach {
                it.join()
            }

        when:
            def loaded = organizationDao.getAllAsync().join()
        then:
            loaded != null
            loaded.size() == orgNames.size()
    }

    private static boolean validate(Organization organization, String resultName) {
        return organization != null &&
            organization.getId() != 0 &&
            StringUtils.equals(organization.getName(), resultName)
    }
}
