package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecificationConfig
import com.ss.jcrm.dictionary.jdbc.test.helper.DictionaryTestHelper
import com.ss.jcrm.integration.test.db.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.DbSpecificationUtils
import com.ss.jcrm.security.config.SecurityConfig
import com.ss.jcrm.security.service.PasswordService
import com.ss.jcrm.user.api.dao.OrganizationDao
import com.ss.jcrm.user.api.dao.UserDao
import com.ss.jcrm.user.api.dao.UserGroupDao
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig
import com.ss.jcrm.user.jdbc.test.helper.UserTestHelper
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource

@Configuration
@Import([
    JdbcUserConfig,
    DbSpecificationConfig,
    JdbcDictionarySpecificationConfig,
    SecurityConfig
])
@PropertySource("classpath:com/ss/jcrm/user/jdbc/test/user-jdbc-test.properties")
class JdbcUserSpecificationConfig {

    @Autowired
    PostgreSQLContainer postgreSQLContainer

    @Autowired
    Environment env

    @Autowired
    PasswordService passwordService

    @Autowired @Lazy
    UserDao userDao

    @Autowired @Lazy
    OrganizationDao organizationDao

    @Autowired @Lazy
    UserGroupDao userRoleDao

    @Autowired
    DictionaryTestHelper dictionaryTestHelper

    @Bean
    @NotNull DataSource userDataSource() {
        return DbSpecificationUtils.newDataSource(
            postgreSQLContainer,
            env.getRequiredProperty("jdbc.user.db.schema")
        )
    }

    @Bean
    @NotNull UserTestHelper userTestHelper() {
        return new UserTestHelper(
            userDao,
            userRoleDao,
            organizationDao,
            passwordService,
            userDataSource(),
            dictionaryTestHelper
        )
    }
}
