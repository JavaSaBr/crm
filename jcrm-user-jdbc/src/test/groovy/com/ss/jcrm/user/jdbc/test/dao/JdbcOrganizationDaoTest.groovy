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

    def "should create a new organization"(String name) {

        when:
            def created = organizationDao.create(name, dictionaryTestHelper.newCountry())
        then:
            validate(created, name)
        where:
            name << ["testOrg1", "testOrg2", "dweffw", "Org1234"]
    }

    def "should create a new organization using async"(String name) {

        when:
            def created = organizationDao.createAsync(name, dictionaryTestHelper.newCountry()).join()
        then:
            validate(created, name)
        where:
            name << ["testOrg1", "testOrg2", "dweffw", "Org1234"]
    }

    def "should create and load a new organization"() {

        given:
            def orgName = "TestOrgName1"
            def created = organizationDao.create(orgName, dictionaryTestHelper.newCountry())
        when:
            def orgByName = organizationDao.findByName(orgName)
            def orgById = organizationDao.findById(created.getId())
        then:
            orgByName != null
            orgByName.getName() == orgName
            orgByName.getId() != 0L
            orgByName.getCountry() != null
            orgById != null
            orgById.getId() != 0L
            orgById.getCountry() != null
    }

    def "should create and load a new organization using async"() {

        given:
            def orgName = "TestOrgName1"
            def created = organizationDao.createAsync(orgName, dictionaryTestHelper.newCountry()).join()
        when:
            def orgByName = organizationDao.findByNameAsync(orgName).join()
            def orgById = organizationDao.findByIdAsync(created.getId()).join()
        then:
            orgByName != null
            orgByName.getName() == orgName
            orgByName.getId() != 0L
            orgByName.getCountry() != null
            orgById != null
            orgById.getId() != 0L
            orgById.getCountry() != null
    }

    def "should load all new organizations"() {

        given:

            def orgNames = ["org1", "org2", "org3", "org4"]

            orgNames.forEach {
                organizationDao.create(it, dictionaryTestHelper.newCountry())
            }

        when:
            def loaded = organizationDao.findAll()
        then:
            loaded != null
            loaded.size() == orgNames.size()
    }

    def "should load all new organizations using async"() {

        given:

            def orgNames = ["org1", "org2", "org3", "org4"]
            def results = new ArrayList<CompletableFuture<?>>()

            orgNames.forEach {
                results.add(organizationDao.createAsync(it, dictionaryTestHelper.newCountry()))
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

    def "should found created organization by name"() {

        given:
            def org = userTestHelper.newOrg()
        when:
            def exist = organizationDao.existByName(org.name)
        then:
            exist
    }

    private static boolean validate(Organization organization, String resultName) {
        return organization != null &&
            organization.getId() != 0 &&
            StringUtils.equals(organization.getName(), resultName) &&
            organization.getCountry() != null
    }
}
