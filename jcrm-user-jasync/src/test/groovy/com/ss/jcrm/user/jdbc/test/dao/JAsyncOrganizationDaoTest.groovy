package com.ss.jcrm.user.jdbc.test.dao

import crm.user.api.Organization
import crm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification
import com.ss.rlib.common.util.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Mono

class JAsyncOrganizationDaoTest extends JAsyncUserSpecification {

    @Autowired
    OrganizationDao organizationDao

    def "should create new organization"(String name) {

        when:
            def created = organizationDao.create(name, dictionaryTestHelper.newCountry()).block()
        then:
            validate(created, name)
        where:
            name << ["testOrg1", "testOrg2", "dweffw", "Org1234"]
    }
    
    def "should create and load new organization"() {

        given:
            def orgName = "TestOrgName1"
            def created = organizationDao.create(orgName, dictionaryTestHelper.newCountry()).block()
        when:
            def orgByName = organizationDao.findByName(orgName).block()
           // def orgById = organizationDao.findById(created.getId()).block()
        then:
            orgByName != null
            orgByName.name() == orgName
            orgByName.id() != 0L
            orgByName.country() != null
           // orgById != null
           // orgById.getId() != 0L
           // orgById.getCountry() != null
    }

    def "should load all new organizations"() {

        given:

            def orgNames = ["org1", "org2", "org3", "org4"]

            orgNames.forEach {
                organizationDao.create(it, dictionaryTestHelper.newCountry()).block()
            }

        when:
            def loaded = organizationDao.findAll().block()
        then:
            loaded != null
            loaded.size() == orgNames.size()
    }

    def "should load all new organizations using async"() {

        given:

            def orgNames = ["org1", "org2", "org3", "org4"]
            def results = new ArrayList<Mono<?>>()

            orgNames.forEach {
                results.add(organizationDao.create(it, dictionaryTestHelper.newCountry()))
            }

            results.forEach {
                it.block()
            }

        when:
            def loaded = organizationDao.findAll().block()
        then:
            loaded != null
            loaded.size() == orgNames.size()
    }

    def "should found created organization by name"() {

        given:
            def org = userTestHelper.newOrg()
        when:
            def exist = organizationDao.existByName(org.name).block()
        then:
            exist
    }

    private static boolean validate(Organization organization, String resultName) {
        return organization != null &&
            organization.id() != 0 &&
            StringUtils.equals(organization.name(), resultName) &&
            organization.country() != null
    }
}
