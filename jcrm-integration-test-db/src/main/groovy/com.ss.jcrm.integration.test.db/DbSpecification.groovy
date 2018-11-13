package com.ss.jcrm.integration.test.db

import com.ss.jcrm.integration.test.DefaultSpecification
import org.springframework.test.context.ContextConfiguration

import javax.sql.DataSource

@ContextConfiguration(classes = DbSpecificationConfig)
class DbSpecification extends DefaultSpecification {

    def clearTable(DataSource dataSource, String... tableNames) {
        tableNames.each {
            executeQuery(dataSource, "DELETE FROM \"$it\"")
        }
    }

    def executeQuery(DataSource dataSource, String query) {

        def connection = dataSource.getConnection()
        connection.withCloseable {
            def statement = it.createStatement()
            statement.withCloseable {
                it.execute(query)
            }
        }
    }
}
