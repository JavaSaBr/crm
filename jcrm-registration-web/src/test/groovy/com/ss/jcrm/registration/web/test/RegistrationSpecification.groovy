package com.ss.jcrm.registration.web.test

import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

import javax.sql.DataSource

@WebAppConfiguration
@ContextConfiguration(classes = RegistrationConfigTest)
class RegistrationSpecification extends WebSpecification {

    @Autowired
    DataSource userDataSource

    def setup() {
        JdbcUserSpecification.clearAllTables(userDataSource)
    }
}
