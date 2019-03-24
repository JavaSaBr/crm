package com.ss.jcrm.user.jdbc.test.helper

import com.ss.jcrm.dictionary.jdbc.test.helper.DictionaryTestHelper
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification

import javax.sql.DataSource

class UserTestHelper {

    class TestUser {

        Long id
        String name
        String password

        TestUser(Long id, String name, String password) {
            this.name = name
            this.password = password
            this.id = id
        }
    }

    private final UserDao userDao
    private final UserGroupDao userRoleDao
    private final OrganizationDao organizationDao
    private final PasswordService passwordService
    private final DataSource userDataSource
    private final DictionaryTestHelper dictionaryTestHelper

    UserTestHelper(
        UserDao userDao,
        UserGroupDao userRoleDao,
        OrganizationDao organizationDao,
        PasswordService passwordService,
        DataSource userDataSource,
        DictionaryTestHelper dictionaryTestHelper
    ) {
        this.userDao = userDao
        this.userRoleDao = userRoleDao
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

    def newOrg() {
        return organizationDao.create(
            System.currentTimeMillis() + "-" + Thread.currentThread().id,
            dictionaryTestHelper.newCountry()
        )
    }

    def newOrg(String name) {
        return organizationDao.create(name, dictionaryTestHelper.newCountry());
    }

    def newUser(String name) {
        return newUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt
        )
    }

    def newUser(String name, String password, byte[] salt) {
        return userDao.create(
            name,
            passwordService.hash(password, salt),
            salt,
            getOrCreateDefaultOrg()
        )
    }

    def newTestUser(String name) {

        def password = passwordService.nextPassword(24)
        def salt = passwordService.nextSalt
        def user = newUser(name, password, salt)

        return new TestUser(user.id, name, password)
    }

    def clearAllData() {
        JdbcUserSpecification.clearAllTables(userDataSource)
    }
}
