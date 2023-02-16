package crm.integration.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    PropertiesTestConfig.class
})
@Configuration(proxyBeanMethods = false)
public class BaseTestConfig {}
