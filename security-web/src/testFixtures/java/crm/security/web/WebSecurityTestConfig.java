package crm.security.web;

import crm.base.web.config.BaseWebConfig;
import crm.integration.test.DefaultSpecification;
import crm.security.AccessRole;
import crm.security.config.SecurityTestConfig;
import crm.security.web.service.TokenService;
import crm.security.web.service.UnsafeTokenService;
import crm.security.web.service.WebRequestSecurityService;
import crm.user.jasync.config.JAsyncUserTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Import({
    JAsyncUserTestConfig.class,
    SecurityTestConfig.class,
    WebSecurityConfig.class,
    BaseWebConfig.class,
    DefaultSpecification.class
})
@PropertySource("classpath:security/web/security-web-test.properties")
@Configuration(proxyBeanMethods = false)
public class WebSecurityTestConfig {

  @Autowired
  WebRequestSecurityService webRequestSecurityService;

  @Bean
  UnsafeTokenService unsafeTokenService(TokenService tokenService) {
    return (UnsafeTokenService) tokenService;
  }

  @Bean
  RouterFunction<ServerResponse> testWebSecurityServiceEndpoints(WebRequestSecurityService webRequestSecurityService) {
    return RouterFunctions
        .route()
        .GET(
            "/web/security/test/authorized",
            request -> webRequestSecurityService
                .isAuthorized(request)
                .flatMap(user -> ServerResponse
                    .ok()
                    .build()))
        .GET(
            "/web/security/test/required/access/role/curator",
            request -> webRequestSecurityService
                .isAuthorized(request, AccessRole.CURATOR)
                .flatMap(user -> ServerResponse
                    .ok()
                    .build()))
        .GET(
            "/web/security/test/required/access/role/curator/or/org_admin",
            request -> webRequestSecurityService
                .isAuthorized(request, AccessRole.ORG_ADMIN, AccessRole.CURATOR)
                .flatMap(user -> ServerResponse
                    .ok()
                    .build()))
        .build();
  }
}
