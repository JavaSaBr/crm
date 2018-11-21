package com.ss.jcrm.user.jdbc.test

import com.ss.jcrm.integration.test.db.DbSpecificationConfig
import com.ss.jcrm.integration.test.db.DbSpecificationUtils
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource

@Configuration
@Import([
    JdbcUserConfig,
    DbSpecificationConfig
])
@PropertySource("classpath:com/ss/jcrm/user/jdbc/test/jcrm-user-jdbc-test.properties")
class JdbcUserSpecificationConfig {

    @Autowired
    private PostgreSQLContainer postgreSQLContainer

    @Value('${jdbc.user.db.schema}')
    private String schema

    @Bean
    @NotNull DataSource userDataSource() {
        return DbSpecificationUtils.newDataSource(postgreSQLContainer, schema)
    }
}
