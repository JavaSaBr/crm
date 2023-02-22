package crm.integration.test.web.config;

import crm.integration.test.config.BaseTestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(BaseTestConfig.class)
@Configuration(proxyBeanMethods = false)
public class WebTestConfig {}
