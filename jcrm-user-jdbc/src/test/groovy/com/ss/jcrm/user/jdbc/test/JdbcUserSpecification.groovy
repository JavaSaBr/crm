package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.integration.test.db.DbSpecification
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JdbcUserSpecificationConfig)
class JdbcUserSpecification extends DbSpecification {

    static final String TABLE_ORGANIZATION = "organization"
    static final String TABLE_USER_ROLE = "user_role"
    static final String TABLE_USER = "user"

}
