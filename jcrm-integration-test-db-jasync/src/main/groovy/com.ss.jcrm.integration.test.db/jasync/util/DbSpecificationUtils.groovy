package com.ss.jcrm.integration.test.db.jasync.util

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.pool.PoolConfiguration
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory
import com.ss.jcrm.integration.test.db.config.DbSpecificationConfig
import com.ss.jcrm.jasync.util.JAsyncUtils
import org.jetbrains.annotations.NotNull
import org.testcontainers.containers.PostgreSQLContainer

class DbSpecificationUtils {
    
    def static clearTable(
        ConnectionPool<? extends ConcreteConnection> connectionPool,
        String schema,
        String... tableNames
    ) {
        tableNames.each {
            executeQuery(connectionPool, "delete from \"$schema\".\"$it\"")
        }
    }
    
    def static executeQuery(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String query
    ) {
        connectionPool.sendQuery(query).join()
    }

    def static newConnectionPool(@NotNull PostgreSQLContainer container, @NotNull String database) {
    
        def address = container.getContainerIpAddress()
        def port = container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)
        
        def configuration = JAsyncUtils.buildConfiguration(
            DbSpecificationConfig.dbUser,
            DbSpecificationConfig.dbPassword,
            address,
            port,
            database,
        )
    
        def connectionPoolConfiguration = JAsyncUtils.buildPoolConfig(
            configuration,
            PoolConfiguration.Default
        )
    
        return new ConnectionPool<>(
            new PostgreSQLConnectionFactory(configuration),
            connectionPoolConfiguration
        )
    }
}
