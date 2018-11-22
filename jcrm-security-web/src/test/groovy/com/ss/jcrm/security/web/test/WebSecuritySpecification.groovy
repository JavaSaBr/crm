package com.ss.jcrm.security.web.test

import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import javax.sql.DataSource

@ContextConfiguration(classes = WebSecuritySpecificationConfig)
class WebSecuritySpecification extends DefaultSpecification {

    @Autowired
    DataSource userDataSource

    def setup() {
        JdbcUserSpecification.clearAllTables(userDataSource)
    }
}
