package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.integration.test.db.DbSpecification
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JdbcUserSpecificationConfig)
class JdbcUserSpecification extends DbSpecification {

}
