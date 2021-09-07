package com.ss.jcrm.security.test

import com.ss.jcrm.integration.test.config.DefaultSpecificationConfig
import com.ss.jcrm.security.config.SecurityConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import([
    DefaultSpecificationConfig,
    SecurityConfig
])
@PropertySource("classpath:com/ss/jcrm/security/test/security-test.properties")
@Configuration
class SecuritySpecificationConfig {

}
