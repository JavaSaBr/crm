package com.ss.jcrm.integration.test.db


import org.flywaydb.core.Flyway
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
class DbSpecificationConfig {

    static final String DB_NAME = "test-db"
    static final String USER = "test-root"
    static final String PWD = "test-root"

    @Bean(destroyMethod = "stop")
    @NotNull PostgreSQLContainer postgreSQLContainer() {

        def container = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName(DB_NAME)
            .withUsername(USER)
            .withPassword(PWD)

        container.start()

        while (!container.isRunning()) {
            Thread.sleep(500)
        }

        return container
    }

    @Bean
    @NotNull Flyway flyway() {

        def container = postgreSQLContainer()
        def address = container.getContainerIpAddress()
        def port = container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)

        def flyway = Flyway.configure()
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .dataSource(
                "jdbc:postgresql://" + address + ":" + port + "/" + DbSpecificationConfig.DB_NAME,
                DbSpecificationConfig.USER,
                DbSpecificationConfig.PWD
            )
            .load()

        flyway.migrate()

        return flyway
    }
}
