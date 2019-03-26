package com.ss.jcrm.registration.web.config;

import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.user.jdbc.config.JdbcUserConfig;
import com.ss.jcrm.web.config.BaseWebConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Import({
    BaseWebConfig.class,
    JdbcUserConfig.class,
    JdbcDictionaryConfig.class,
    WebSecurityConfig.class,
})
@Configuration
@ComponentScan("com.ss.jcrm.registration.web")
@PropertySource("classpath:com/ss/jcrm/registration/web/registration-web.properties")
public class RegistrationWebConfig {

    @Autowired
    private Environment env;

    @Bean
    @NotNull ResourceValidator resourceValidator() {
        return new ResourceValidator(env);
    }
}
