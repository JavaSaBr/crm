package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.integration.test.db.DbSpecification
import com.ss.jcrm.integration.test.db.DbSpecificationConfig
import com.ss.jcrm.jdbc.ConnectionPoolFactory
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig
import org.flywaydb.core.Flyway
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.PropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource

@Configuration
@Import([
    JdbcUserConfig,
    DbSpecificationConfig
])
@ContextConfiguration(classes = [
    JdbcUserConfig,
    DbSpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/user/jdbc/test/jcrm-user-jdbc-test.properties")
class JdbcUserSpecificationConfig extends DbSpecification {

    @Autowired
    private PostgreSQLContainer postgreSQLContainer

    @Value('${jdbc.user.db.schema}')
    private String schema

    @Bean
    @NotNull DataSource userDataSource() {

        def address = postgreSQLContainer.getContainerIpAddress()
        def port = postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)

        return ConnectionPoolFactory.newDataSource(
            "jdbc:postgresql://" + address + ":" + port + "/" + DbSpecificationConfig.DB_NAME,
            schema,
            DbSpecificationConfig.USER,
            DbSpecificationConfig.PWD
        )
    }

    @Bean
    @NotNull Flyway userFlyway() {

        def address = postgreSQLContainer.getContainerIpAddress()
        def port = postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)

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
