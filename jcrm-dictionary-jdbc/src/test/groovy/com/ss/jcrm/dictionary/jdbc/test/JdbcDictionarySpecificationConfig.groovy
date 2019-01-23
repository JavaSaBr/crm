package com.ss.jcrm.dictionary.jdbc.test

import com.ss.jcrm.integration.test.db.DbSpecificationConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@Import([
    //JdbcUserConfig,
    DbSpecificationConfig,
    //SecurityConfig
])
@PropertySource("classpath:com/ss/jcrm/user/jdbc/test/user-jdbc-test.properties")
class JdbcDictionarySpecificationConfig {

    @Autowired
    PostgreSQLContainer postgreSQLContainer

    @Autowired
    Environment env
/*
    @Autowired
    PasswordService passwordService

    @Autowired @Lazy
    UserDao userDao

    @Autowired @Lazy
    OrganizationDao organizationDao

    @Autowired @Lazy
    UserRoleDao userRoleDao

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
            userDataSource()
        )
    }*/
}
