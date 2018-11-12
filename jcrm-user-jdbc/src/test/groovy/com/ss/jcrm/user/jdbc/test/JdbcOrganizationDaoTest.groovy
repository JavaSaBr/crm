package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.user.api.Organization
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.rlib.common.util.StringUtils
import org.springframework.beans.factory.annotation.Autowired

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
            "weffwewefwefwefwef"   | "weffwewefwefwefwef"
    }

    private static boolean validate(Organization organization, String resultName) {
        return organization != null &&
            organization.getId() != 0 &&
            StringUtils.equals(organization.getName(), resultName)
    }
}
