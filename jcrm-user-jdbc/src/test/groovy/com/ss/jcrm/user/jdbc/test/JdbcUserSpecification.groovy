package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.integration.test.DefaultSpecification
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JdbcUserSpecificationConfig)
class JdbcUserSpecification extends DefaultSpecification {

    static final String TABLE_ORGANIZATION = "organization"
    static final String TABLE_USER_ROLE = "user_role"
    static final String TABLE_USER = "user"
}
