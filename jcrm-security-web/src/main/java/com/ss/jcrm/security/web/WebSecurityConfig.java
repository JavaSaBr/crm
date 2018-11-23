package com.ss.jcrm.security.web;

import com.ss.jcrm.security.config.SecurityConfig;
import com.ss.jcrm.security.web.service.TokenService;
import com.ss.jcrm.security.web.service.impl.JjwtTokenService;
import com.ss.jcrm.user.api.dao.UserDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@Configuration
@Import(SecurityConfig.class)
public class WebSecurityConfig {

    @Autowired
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Bean
    @NotNull TokenService tokenGenerator() {
        return new JjwtTokenService(
            userDao,
            env.getRequiredProperty("security.token.secret.key"),
            env.getProperty("token.expiration.time", Integer.class, 600)
        );
    }
}
