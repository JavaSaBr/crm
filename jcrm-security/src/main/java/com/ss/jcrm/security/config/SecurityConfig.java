package com.ss.jcrm.security.config;

import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.service.impl.DefaultPasswordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/security/security.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/security/security-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@Configuration
public class SecurityConfig {

    @Autowired
    private Environment env;

    @Bean
    @NotNull PasswordService passwordService() {
        return new DefaultPasswordService(
            env.getProperty("security.password.key.iterations", Integer.class, 10000),
            env.getProperty("security.password.key.length", Integer.class, 256)
        );
    }
}
