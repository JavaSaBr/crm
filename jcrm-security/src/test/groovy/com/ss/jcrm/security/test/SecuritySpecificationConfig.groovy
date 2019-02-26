package com.ss.jcrm.security.test

import com.ss.jcrm.integration.test.DefaultSpecificationConfig
import com.ss.jcrm.security.config.SecurityConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import([
    DefaultSpecificationConfig,
    SecurityConfig
])
class SecuritySpecificationConfig {

}
