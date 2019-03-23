package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.dictionary.jdbc.test.helper.DictionaryTestHelper
import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.user.jdbc.test.helper.UserTestHelper
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import javax.sql.DataSource

import static com.ss.jcrm.integration.test.db.DbSpecificationUtils.clearTable

@ContextConfiguration(classes = JdbcUserSpecificationConfig)
class JdbcUserSpecification extends DefaultSpecification {

    static final String TABLE_ORGANIZATION = "organization"
    static final String TABLE_USER_ROLE = "user_role"
    static final String TABLE_USER = "user"

    def static clearAllTables(@NotNull DataSource userDataSource) {
        clearTable(userDataSource, TABLE_USER, TABLE_USER_ROLE, TABLE_ORGANIZATION)
    }

    @Autowired
    DataSource userDataSource

    @Autowired
    DictionaryTestHelper dictionaryTestHelper

    @Autowired
    UserTestHelper userTestHelper

    def setup() {
        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()
    }
}
