package crm.security.web;

import crm.security.config.SecurityConfig;
import crm.security.web.exception.handler.SecurityWebExceptionHandler;
import crm.security.web.service.TokenService;
import crm.security.web.service.WebRequestSecurityService;
import crm.security.web.service.impl.DefaultWebRequestSecurityService;
import crm.security.web.service.impl.JjwtTokenService;
import crm.user.api.dao.UserDao;
import com.ss.rlib.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@PropertySources({
    @PropertySource("classpath:security/web/security-web.properties"),
    @PropertySource(value = "classpath:security/web/security-web-${spring.profiles.active:default}.properties", ignoreResourceNotFound = true)
})
@Import(SecurityConfig.class)
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

  @Bean
  @ConditionalOnBean(UserDao.class)
  @NotNull TokenService tokenService(
      @Value("${security.token.secret.key.path:}") @NotNull String secretKeyPath,
      @Value("${security.token.secret.key:}") @NotNull String secretKeyHex,
      @Value("${token.expiration.time:30}") int expirationTime,
      @Value("${token.max.refreshes:60}") int maxRefreshes,
      @NotNull UserDao userDao) throws IOException {

    byte[] secretKey = null;

    if (!StringUtils.isEmpty(secretKeyPath)) {

      var path = Paths.get(secretKeyPath);

      if (Files.exists(path)) {
        log.info("Read a token secret key from the file: {}", path);
        secretKey = Files.readAllBytes(path);
      } else {
        log.error("Can't read the token secret file: {}", path);
      }

    } else {
      secretKey = StringUtils.hexStringToBytes(secretKeyHex);
      log.info("Read a token secret key from string property.");
    }

    if (secretKey == null) {
      throw new IllegalStateException("Token's secret key is not setup.");
    }

    return new JjwtTokenService(userDao, secretKey, expirationTime, maxRefreshes);
  }

  @Bean
  @NotNull WebFilter corsWebFilter(
      @Value("${cors.allow.origin:}") @NotNull String allowOrigin,
      @Value("${cors.allow.methods:}") @NotNull String allowMethods,
      @Value("${cors.max.age:}") @NotNull String maxAge,
      @Value("${cors.allow.headers:}") @NotNull String allowHeaders) {

    if (allowHeaders.isEmpty() || maxAge.isEmpty() || allowMethods.isEmpty() || allowOrigin.isEmpty()) {
      return (exchange, chain) -> chain.filter(exchange);
    }

    return (exchange, chain) -> {

      var request = exchange.getRequest();
      var uri = request.getURI();
      var actualScheme = uri.getScheme();

      if (actualScheme == null || CorsUtils.isCorsRequest(request)) {

        var response = exchange.getResponse();
        var headers = response.getHeaders();
        headers.add("Access-Control-Allow-Origin", allowOrigin);
        headers.add("Access-Control-Allow-Methods", allowMethods);
        headers.add("Access-Control-Max-Age", maxAge);
        headers.add("Access-Control-Allow-Headers", allowHeaders);

        if (request.getMethod() == HttpMethod.OPTIONS) {
          response.setStatusCode(HttpStatus.OK);
          return Mono.empty();
        }
      }

      return chain.filter(exchange);
    };
  }

  @Bean
  @NotNull WebExceptionHandler securityWebExceptionHandler() {
    return new SecurityWebExceptionHandler();
  }

  @Bean
  @ConditionalOnBean(UserDao.class)
  @NotNull WebRequestSecurityService webRequestSecurityService(@NotNull TokenService tokenService) {
    return new DefaultWebRequestSecurityService(tokenService);
  }
}
