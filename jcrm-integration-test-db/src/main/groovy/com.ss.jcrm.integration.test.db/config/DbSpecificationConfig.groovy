package com.ss.jcrm.integration.test.db.config

import com.ss.jcrm.integration.test.config.DefaultSpecificationConfig
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT

@Configuration
@Import(DefaultSpecificationConfig)
class DbSpecificationConfig {

    static final String DB_NAME = "test-db"
    static final String USER = "test-root"
    static final String PWD = "test-root"

    @Bean(destroyMethod = "stop")
    @NotNull PostgreSQLContainer postgreSQLContainer() {

        def container = new PostgreSQLContainer("postgres:12")
            .withDatabaseName(DB_NAME)
            .withUsername(USER)
            .withPassword(PWD)

        container.start()

        while (!container.isRunning()) {
            Thread.sleep(500)
        }
    
        def mappedPort = container.getMappedPort(POSTGRESQL_PORT)
        
        System.setProperty("db.test.url", "jdbc:postgresql://${container.getContainerIpAddress()}:${mappedPort}/$DB_NAME")
        System.setProperty("db.test.username", USER)
        System.setProperty("db.test.password", PWD)

        return container
    }
}
