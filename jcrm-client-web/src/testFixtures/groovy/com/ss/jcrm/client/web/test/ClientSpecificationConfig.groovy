package com.ss.jcrm.client.web.test

import com.ss.jcrm.client.jasync.test.JAsyncClientSpecificationConfig
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import user.jasync.JAsyncUserSpecificationConfig
import con.ss.jcrm.client.web.config.ClientWebConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    ClientWebConfig,
    DbSpecificationConfig,
    JAsyncDictionarySpecificationConfig,
    JAsyncUserSpecificationConfig,
    JAsyncClientSpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/client/web/test/client-web-test.properties")
class ClientSpecificationConfig {
}
