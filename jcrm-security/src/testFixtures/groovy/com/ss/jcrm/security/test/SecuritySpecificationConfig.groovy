package com.ss.jcrm.security.test

import com.ss.jcrm.security.config.SecurityConfig
import crm.integration.test.config.BaseTestConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import([
    BaseTestConfig,
    SecurityConfig
])
@PropertySource("classpath:com/ss/jcrm/security/test/security-test.properties")
@Configuration
class SecuritySpecificationConfig {

}
