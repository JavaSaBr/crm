package com.ss.jcrm.registration.web.config;

import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig;
import com.ss.jcrm.web.config.BaseWebConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    BaseWebConfig.class,
    JdbcUserConfig.class,
    WebSecurityConfig.class
})
@Configuration
@ComponentScan("com.ss.jcrm.registration.web")
public class RegistrationWebConfig {
}
