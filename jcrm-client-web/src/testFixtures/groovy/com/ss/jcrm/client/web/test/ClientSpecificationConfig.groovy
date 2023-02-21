package com.ss.jcrm.client.web.test

import crm.client.jasync.config.JAsyncClientTestConfig
import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig

import con.ss.jcrm.client.web.config.ClientWebConfig
import crm.integration.test.db.config.DbTestConfig
import crm.user.jasync.config.JAsyncUserTestConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    ClientWebConfig,
    DbTestConfig,
    JAsyncDictionaryTestConfig,
    JAsyncUserTestConfig,
    JAsyncClientTestConfig
])
@PropertySource("classpath:com/ss/jcrm/client/web/test/client-web-test.properties")
class ClientSpecificationConfig {
}
