package com.ss.jcrm.integration.test.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import([
    PropertiesTestConfig
])
@Configuration(proxyBeanMethods = false)
class BaseTestConfig {

}

