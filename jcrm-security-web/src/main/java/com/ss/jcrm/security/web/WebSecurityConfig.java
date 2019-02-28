package com.ss.jcrm.security.web;

import com.ss.jcrm.security.config.SecurityConfig;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.security.web.service.impl.JjwtTokenService;
import com.ss.jcrm.user.api.dao.UserDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@Import(SecurityConfig.class)
public class WebSecurityConfig {

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean @Lazy
    @NotNull TokenService tokenGenerator() {
        return new JjwtTokenService(
            applicationContext.getBean(UserDao.class),
            env.getRequiredProperty("security.token.secret.key"),
            env.getProperty("token.expiration.time", Integer.class, 600)
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
}
