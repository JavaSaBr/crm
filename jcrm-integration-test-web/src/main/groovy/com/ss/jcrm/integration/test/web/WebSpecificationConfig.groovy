package com.ss.jcrm.integration.test.web

import crm.integration.test.config.BaseTestConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(BaseTestConfig)
@Configuration(proxyBeanMethods = false)
class WebSpecificationConfig {

}
