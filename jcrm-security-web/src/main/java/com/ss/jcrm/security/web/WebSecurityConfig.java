package com.ss.jcrm.security.web;

import com.ss.jcrm.security.web.token.TokenService;
import com.ss.jcrm.security.web.token.impl.JjwtTokenService;
import com.ss.jcrm.user.api.dao.UserDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Bean
    @NotNull TokenService tokenGenerator() {
        return new JjwtTokenService(
            userDao,
            env.getProperty("token.expiration.time", Integer.class, 600)
        );
    }
}
