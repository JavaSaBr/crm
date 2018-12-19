package com.ss.jcrm.dictionary.jdbc.test

import com.ss.jcrm.integration.test.DefaultSpecification
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import javax.sql.DataSource

import static com.ss.jcrm.integration.test.db.DbSpecificationUtils.clearTable

@ContextConfiguration(classes = JdbcDictionarySpecificationConfig)
class JdbcDictionarySpecification extends DefaultSpecification {

    static final String TABLE_COUNTRY = "country"

    def static clearAllTables(@NotNull DataSource dictionaryDataSource) {
        clearTable(dictionaryDataSource, TABLE_COUNTRY)
    }

    @Autowired
    DataSource dictionaryDataSource

    def setup() {
        clearAllTables(dictionaryDataSource)
    }
}
