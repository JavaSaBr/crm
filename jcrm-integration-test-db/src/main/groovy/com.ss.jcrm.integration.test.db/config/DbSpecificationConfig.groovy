package com.ss.jcrm.integration.test.db.config

import com.ss.jcrm.integration.test.config.DefaultSpecificationConfig
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT

@Import(DefaultSpecificationConfig)
@Configuration(proxyBeanMethods = false)
class DbSpecificationConfig {

    static final String dbName = "test-db"
    static final String dbUser = "test-root"
    static final String dbPassword = "test-root"

    @Bean(destroyMethod = "stop")
    @NotNull PostgreSQLContainer postgreSQLContainer() {

        def container = new PostgreSQLContainer("postgres:12")
            .withDatabaseName(dbName)
            .withUsername(dbUser)
            .withPassword(dbPassword)

        container.start()

        while (!container.isRunning()) {
            Thread.sleep(500)
        }
    
        def mappedPort = container.getMappedPort(POSTGRESQL_PORT)
        
        System.setProperty("db.test.url", "jdbc:postgresql://${container.getContainerIpAddress()}:${mappedPort}/$dbName")
        System.setProperty("db.test.username", dbUser)
        System.setProperty("db.test.password", dbPassword)

        return container
    }
}
