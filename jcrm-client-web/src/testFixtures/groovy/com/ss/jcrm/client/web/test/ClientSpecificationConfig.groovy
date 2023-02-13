package com.ss.jcrm.client.web.test

import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig
import crm.client.jasync.config.JAsyncClientTestConfig
import integration.test.db.config.DbTestConfig

import con.ss.jcrm.client.web.config.ClientWebConfig
import crm.user.jasync.config.JAsyncUserTestConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    ClientWebConfig,
    DbTestConfig,
    JAsyncDictionarySpecificationConfig,
    JAsyncUserTestConfig,
    JAsyncClientTestConfig
])
@PropertySource("classpath:com/ss/jcrm/client/web/test/client-web-test.properties")
class ClientSpecificationConfig {
}
