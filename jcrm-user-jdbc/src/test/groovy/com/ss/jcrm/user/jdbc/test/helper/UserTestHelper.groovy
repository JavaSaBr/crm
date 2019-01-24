package com.ss.jcrm.user.jdbc.test.helper

import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserRoleDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.jetbrains.annotations.NotNull

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
    private final UserRoleDao userRoleDao
    private final OrganizationDao organizationDao
    private final PasswordService passwordService
    private final DataSource userDataSource

    UserTestHelper(
        @NotNull UserDao userDao,
        @NotNull UserRoleDao userRoleDao,
        @NotNull OrganizationDao organizationDao,
        @NotNull PasswordService passwordService,
        @NotNull DataSource userDataSource
    ) {
        this.userDao = userDao
        this.userRoleDao = userRoleDao
        this.organizationDao = organizationDao
        this.passwordService = passwordService
        this.userDataSource = userDataSource
    }

    def synchronized getOrCreateDefaultOrg() {

        def org = organizationDao.findByName("TestOrg")

        if (org == null) {
            return organizationDao.create("TestOrg")
        }

        return org
    }

    def newDaoUser(@NotNull String name) {
        return newDaoUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt
        )
    }

    def newDaoUser(@NotNull String name, @NotNull String password, @NotNull byte[] salt) {
        return userDao.create(
            name,
            passwordService.hash(password, salt),
            salt,
            getOrCreateDefaultOrg()
        )
    }

    def newTestUser(@NotNull String name) {

        def password = passwordService.nextPassword(24)
        def salt = passwordService.nextSalt
        def user = newDaoUser(name, password, salt)

        return new TestUser(user.id, name, password)
    }

    def clearAllData() {
        JdbcUserSpecification.clearAllTables(userDataSource)
    }
}
