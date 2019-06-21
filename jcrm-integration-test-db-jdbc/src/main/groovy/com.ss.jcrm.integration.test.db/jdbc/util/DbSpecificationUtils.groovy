package com.ss.jcrm.integration.test.db.jdbc.util

import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.jdbc.ConnectionPoolFactory
import org.jetbrains.annotations.NotNull
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource

class DbSpecificationUtils {

    def static clearTable(@NotNull DataSource dataSource, @NotNull String... tableNames) {
        tableNames.each {
            executeQuery(dataSource, "DELETE FROM \"$it\"")
        }
    }

    def static executeQuery(@NotNull  DataSource dataSource, @NotNull  String query) {

        def connection = dataSource.getConnection()
        connection.withCloseable {
            def statement = it.createStatement()
            statement.withCloseable {
                it.execute(query)
            }
        }
    }

    def static newDataSource(@NotNull PostgreSQLContainer container, @NotNull String schema) {

        def address = container.getContainerIpAddress()
        def port = container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)

        return ConnectionPoolFactory.newDataSource(
            "jdbc:postgresql://" + address + ":" + port + "/" + DbSpecificationConfig.DB_NAME,
            schema,
            DbSpecificationConfig.USER,
            DbSpecificationConfig.PWD
        )
    }
}
