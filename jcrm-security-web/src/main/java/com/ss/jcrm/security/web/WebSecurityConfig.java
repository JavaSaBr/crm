package com.ss.jcrm.security.web;

import com.ss.jcrm.security.config.SecurityConfig;
import com.ss.jcrm.security.web.exception.handler.SecurityWebExceptionHandler;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.security.web.service.impl.DefaultWebRequestSecurityService;
import com.ss.jcrm.security.web.service.impl.JjwtTokenService;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.rlib.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
@Configuration
@Import(SecurityConfig.class)
public class WebSecurityConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConditionalOnBean(UserDao.class)
    @NotNull TokenService tokenService(@NotNull UserDao userDao) throws IOException {

        byte[] secretKey = null;

        var secretKeyPath = env.getProperty("security.token.secret.key.path");

        if (!StringUtils.isEmpty(secretKeyPath)) {

            var path = Paths.get(secretKeyPath);

            if (Files.exists(path)) {
                log.info("Read a token secret key from the file: {}", path);
                secretKey = Files.readAllBytes(path);
            } else {
                log.error("Can't read the token secret file: {}", path);
            }

        } else {
            secretKey = StringUtils.hexStringToBytes(env.getRequiredProperty("security.token.secret.key"));
            log.info("Read a token secret key from string property.");
        }

        if (secretKey == null) {
            throw new IllegalStateException("Token's secret key is not setup.");
        }

        return new JjwtTokenService(
            userDao,
            secretKey,
            env.getProperty("token.expiration.time", int.class, 30),
            env.getProperty("token.max.refreshes", int.class, 60)
        );
    }

    @Bean
    @NotNull WebFilter corsWebFilter() {

        var allowOrigin = env.getProperty("cors.allow.origin", String.class, null);
        var allowMethods = env.getProperty("cors.allow.methods", String.class, null);
        var maxAge = env.getProperty("cors.max.age", String.class, null);
        var allowHeaders = env.getProperty("cors.allow.headers", String.class, null);

        if (allowHeaders == null || maxAge == null || allowMethods == null || allowOrigin == null) {
            return (exchange, chain) -> chain.filter(exchange);
        }

        return (exchange, chain) -> {

            var request = exchange.getRequest();

            if (CorsUtils.isCorsRequest(request)) {

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
