package crm.security.config;

import crm.integration.test.config.BaseTestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({
    BaseTestConfig.class,
    SecurityConfig.class
})
@PropertySource("classpath:security/security-test.properties")
@Configuration(proxyBeanMethods = false)
public class SecurityTestConfig {}
