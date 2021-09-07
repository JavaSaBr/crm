package com.ss.jcrm.dictionary.web.test

import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig
import com.ss.jcrm.dictionary.web.config.DictionaryWebConfig
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.integration.test.web.WebSpecificationConfig
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    DictionaryWebConfig,
    DbSpecificationConfig,
    WebSpecificationConfig,
    JAsyncDictionarySpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/dictionary/web/test/dictionary-web-test.properties")
class DictionarySpecificationConfig {

    @Autowired
    Flyway flyway
}
