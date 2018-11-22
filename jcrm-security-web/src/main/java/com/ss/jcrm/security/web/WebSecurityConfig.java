package com.ss.jcrm.security.web;

import com.ss.jcrm.security.web.token.TokenGenerator;
import com.ss.jcrm.security.web.token.impl.JjwtTokenGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityConfig {

    @Bean
    @NotNull TokenGenerator tokenGenerator() {
        return new JjwtTokenGenerator();
    }
}
