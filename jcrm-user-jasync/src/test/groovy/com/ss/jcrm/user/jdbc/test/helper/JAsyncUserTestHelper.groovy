package com.ss.jcrm.user.jdbc.test.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.EmailConfirmation
import com.ss.jcrm.user.api.Organization
import com.ss.jcrm.user.api.User
import com.ss.jcrm.user.api.UserGroup
import com.ss.jcrm.user.api.dao.EmailConfirmationDao
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.api.test.UserTestHelper
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecification

import java.time.Instant
import java.util.concurrent.ThreadLocalRandom

class JAsyncUserTestHelper implements UserTestHelper {
    
    private final ConnectionPool<? extends ConcreteConnection> connectionPool
    private final String schema
    private final UserDao userDao
    private final UserGroupDao userGroupDao
    private final OrganizationDao organizationDao
    private final PasswordService passwordService
    private final DictionaryTestHelper dictionaryTestHelper
    private final EmailConfirmationDao emailConfirmationDao
    
    JAsyncUserTestHelper(
        ConnectionPool<? extends ConcreteConnection> connectionPool,
        String schema,
        UserDao userDao,
        UserGroupDao userGroupDao,
        OrganizationDao organizationDao,
        PasswordService passwordService,
        DictionaryTestHelper dictionaryTestHelper,
        EmailConfirmationDao emailConfirmationDao
    ) {
        this.connectionPool = connectionPool
        this.schema = schema
        this.userDao = userDao
        this.userGroupDao = userGroupDao
        this.organizationDao = organizationDao
        this.passwordService = passwordService
        this.dictionaryTestHelper = dictionaryTestHelper
        this.emailConfirmationDao = emailConfirmationDao
    }

    def synchronized getOrCreateDefaultOrg() {

        def org = organizationDao.findByName("TestOrg").block()

        if (org == null) {
            return organizationDao.create("TestOrg", dictionaryTestHelper.newCountry()).block()
        }

        return org
    }
    
    @Override
    Set<AccessRole> onlyOrgAdminRole() {
        return Set.of(AccessRole.ORG_ADMIN)
    }

    @Override
    Organization newOrg() {
        return organizationDao.create(
            nextUId(),
            dictionaryTestHelper.newCountry()
        ).block()
    }

    def newOrg(String name) {
        return organizationDao.create(name, dictionaryTestHelper.newCountry()).block()
    }

    def newGroup(String name, Organization organization) {
        return userGroupDao.create(name, organization).block()
    }
    
    @Override
    UserGroup newGroup(Organization organization) {
        return userGroupDao.create(nextUId(), organization).block()
    }

    @Override
    User newUser() {
        return newUser(
            nextEmail(),
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            getOrCreateDefaultOrg()
        )
    }

    @Override
    User newUser(String name) {
        return newUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            getOrCreateDefaultOrg()
        )
    }
    
    @Override
    User newUser(String name, String phoneNumber) {
        return newUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            getOrCreateDefaultOrg(),
            phoneNumber
        )
    }
    
    User newUser(String name, String phoneNumber, String password) {
        return newUser(
            name,
            password,
            passwordService.nextSalt,
            getOrCreateDefaultOrg(),
            phoneNumber
        )
    }
    
    @Override
    User newUser(String name, Organization organization) {
        return newUser(
            name,
            passwordService.nextPassword(24),
            passwordService.nextSalt,
            organization
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
        ).block()
    }
    
    def newUser(String name, String password, byte[] salt, Organization organization, String phoneNumber) {
        return userDao.create(
            name,
            passwordService.hash(password, salt),
            salt,
            organization,
            onlyOrgAdminRole(),
            null,
            null,
            null,
            phoneNumber
        ).block()
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
        ).block()
    }

    @Override
    void clearAllData() {
        JAsyncUserSpecification.clearAllTables(connectionPool, schema)
    }
    
    @Override
    String nextUId() {
        return System.nanoTime() + "-" + Thread.currentThread().id
    }
    
    def nextCode() {
    
        def code = String.valueOf(System.nanoTime())
    
        if (code.length() > 14) {
            return code.substring(0, 14);
        }
    
        return code
    }
    
    @Override
    String nextEmail() {
        return ThreadLocalRandom.current()
            .nextInt(9) + "-" + (Thread.currentThread().id % 10) + "@tt.co"
    }
    
    @Override
    EmailConfirmation newEmailConfirmation() {
        return emailConfirmationDao.create(
            nextCode(),
            nextEmail(),
            Instant.now() + 60
        ).block()
    }
}
