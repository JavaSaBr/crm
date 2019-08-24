package com.ss.jcrm.security.web.test

import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.security.web.WebSecurityConfig
import com.ss.jcrm.security.web.service.TokenService
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.user.jdbc.test.JAsyncUserSpecificationConfig
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([
    JAsyncUserSpecificationConfig,
    WebSecurityConfig,
    DefaultSpecification
])
@PropertySource("classpath:com/ss/jcrm/security/web/test/security-web-test.properties")
class WebSecuritySpecificationConfig {

    @Autowired
    TokenService tokenService

    @Bean
    @NotNull UnsafeTokenService unsafeTokenService() {
        return tokenService as UnsafeTokenService
    }
}
