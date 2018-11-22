package com.ss.jcrm.security.web.test

import com.ss.jcrm.security.web.WebSecurityConfig
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecificationConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    JdbcUserSpecificationConfig,
    WebSecurityConfig
])
@PropertySource("classpath:com/ss/jcrm/security/web/test/jcrm-web-security-test.properties")
class WebSecuritySpecificationConfig {

}
