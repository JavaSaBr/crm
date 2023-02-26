package crm.jasync.config;

import crm.dao.FlywaysConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    FlywaysConfig.class
})
@Configuration(proxyBeanMethods = false)
public class JAsyncTestConfig {}
