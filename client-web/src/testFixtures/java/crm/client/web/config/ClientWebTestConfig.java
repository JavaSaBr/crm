package crm.client.web.config;

import crm.client.jasync.config.JAsyncClientTestConfig;
import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig;
import crm.integration.test.db.config.DbTestConfig;
import crm.user.jasync.config.JAsyncUserTestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({
    ClientWebConfig.class,
    DbTestConfig.class,
    JAsyncDictionaryTestConfig.class,
    JAsyncUserTestConfig.class,
    JAsyncClientTestConfig.class
})
@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:client/web/client-web-test.properties")
public class ClientWebTestConfig {}
