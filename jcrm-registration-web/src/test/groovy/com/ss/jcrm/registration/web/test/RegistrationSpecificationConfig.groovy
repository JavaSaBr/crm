package com.ss.jcrm.registration.web.test

import com.ss.jcrm.integration.test.db.DbSpecificationConfig
import com.ss.jcrm.registration.web.config.RegistrationConfig
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecificationConfig
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    RegistrationConfig,
    DbSpecificationConfig,
    JdbcUserSpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/registration/web/test/registration-web-test.properties")
class RegistrationSpecificationConfig {

    @Autowired
    Flyway flyway
}
