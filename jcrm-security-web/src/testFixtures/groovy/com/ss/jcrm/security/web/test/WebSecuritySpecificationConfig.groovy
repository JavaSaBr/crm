package com.ss.jcrm.security.web.test

import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.security.test.SecuritySpecificationConfig
import com.ss.jcrm.security.web.WebSecurityConfig
import com.ss.jcrm.security.web.service.TokenService
import com.ss.jcrm.security.web.service.UnsafeTokenService
import com.ss.jcrm.security.web.service.WebRequestSecurityService

import crm.base.web.config.BaseWebConfig
import crm.user.jasync.config.JAsyncUserTestConfig
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
@Import([
    JAsyncUserTestConfig,
    SecuritySpecificationConfig,
    WebSecurityConfig,
    BaseWebConfig,
    DefaultSpecification
])
@PropertySource("classpath:com/ss/jcrm/security/web/test/security-web-test.properties")
class WebSecuritySpecificationConfig {

    @Autowired
    TokenService tokenService
    
    @Autowired
    WebRequestSecurityService webRequestSecurityService
    
    @Bean
    @NotNull UnsafeTokenService unsafeTokenService() {
        return tokenService as UnsafeTokenService
    }
    
    @Bean
    RouterFunction<ServerResponse> testWebSecurityServiceEndpoints() {
        return RouterFunctions.route()
            .GET("/web/security/test/authorized", { request ->
                return webRequestSecurityService.isAuthorized(request)
                    .flatMap({ ServerResponse.ok().build() })
            })
            .GET("/web/security/test/required/access/role/curator", { request ->
                return webRequestSecurityService.isAuthorized(request, AccessRole.CURATOR)
                    .flatMap({ ServerResponse.ok().build() })
            })
            .GET("/web/security/test/required/access/role/curator/or/org_admin", { request ->
                return webRequestSecurityService.isAuthorized(request, AccessRole.ORG_ADMIN, AccessRole.CURATOR)
                    .flatMap({ ServerResponse.ok().build() })
            })
            .build()
    }
}
