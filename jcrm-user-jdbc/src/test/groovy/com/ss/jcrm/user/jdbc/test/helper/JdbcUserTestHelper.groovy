package com.ss.jcrm.user.jdbc.test.helper

import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.Organization
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.api.test.UserTestHelper
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification

import javax.sql.DataSource

class JdbcUserTestHelper implements UserTestHelper {

    private final UserDao userDao
    private final UserGroupDao userGroupDao
    private final OrganizationDao organizationDao
    private final PasswordService passwordService
    private final DataSource userDataSource
    private final DictionaryTestHelper dictionaryTestHelper

    JdbcUserTestHelper(
        UserDao userDao,
        UserGroupDao userGroupDao,
        OrganizationDao organizationDao,
        PasswordService passwordService,
        DataSource userDataSource,
        DictionaryTestHelper dictionaryTestHelper
    ) {
        this.userDao = userDao
        this.userGroupDao = userGroupDao
        this.organizationDao = organizationDao
        this.passwordService = passwordService
        this.userDataSource = userDataSource
        this.dictionaryTestHelper = dictionaryTestHelper
    }

    def synchronized getOrCreateDefaultOrg() {

        def org = organizationDao.findByName("TestOrg")

        if (org == null) {
            return organizationDao.create("TestOrg", dictionaryTestHelper.newCountry())
        }

        return org
    }

    def onlyOrgAdminRole() {
        return Set.of(AccessRole.ORG_ADMIN)
    }

    @Override
    Organization newOrg() {
        return organizationDao.create(
            nextUId(),
            dictionaryTestHelper.newCountry()
        )
    }

    def newOrg(String name) {
        return organizationDao.create(name, dictionaryTestHelper.newCountry());
    }

    def newGroup(String name, Organization organization) {
        return userGroupDao.create(name, organization)
    }

    def newGroup(Organization organization) {
        return userGroupDao.create(nextUId(), organization)
    }

    def newUser(String name) {
        return newUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            getOrCreateDefaultOrg()
        )
    }

    def newUser(String name, Organization organization) {
        return newUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            organization
        )
    }

    def newUserUsingAsync(String name) {
        return newUserUsingAsync(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            getOrCreateDefaultOrg()
        )
    }

    def newUser(String name, String password, byte[] salt, Organization organization) {
        return userDao.create(
            name,
            passwordService.hash(password, salt),
            salt,
            organization,
            onlyOrgAdminRole(),
            null,
            null,
            null,
            null
        )
    }

    def newUserUsingAsync(String name, String password, byte[] salt, Organization organization) {
        return userDao.createAsync(
            name,
            passwordService.hash(password, salt),
            salt,
            organization,
            onlyOrgAdminRole(),
            null,
            null,
            null,
            null
        ).join()
    }

    def newOrgAdmin() {

        def password = passwordService.nextPassword(24);
        def salt = passwordService.nextSalt

        return userDao.create(
            nextUId(),
            passwordService.hash(password, salt),
            salt,
            getOrCreateDefaultOrg(),
            onlyOrgAdminRole(),
            null,
            null,
            null,
            null
        )
    }

    @Override
    void clearAllData() {
        JdbcUserSpecification.clearAllTables(userDataSource)
    }

    @Override
    String nextUId() {
        return System.nanoTime() + "-" + Thread.currentThread().id
    }
}
