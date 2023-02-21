package com.ss.jcrm.dictionary.web.test

import com.ss.jcrm.dictionary.web.config.DictionaryWebConfig
import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig
import com.ss.jcrm.integration.test.web.WebSpecificationConfig
import crm.integration.test.db.config.DbTestConfig
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    DictionaryWebConfig,
    DbTestConfig,
    WebSpecificationConfig,
    JAsyncDictionaryTestConfig
])
@PropertySource("classpath:com/ss/jcrm/dictionary/web/test/dictionary-web-test.properties")
class DictionarySpecificationConfig {

    @Autowired
    Flyway flyway
}
