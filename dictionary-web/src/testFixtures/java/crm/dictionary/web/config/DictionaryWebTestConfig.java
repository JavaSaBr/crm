package crm.dictionary.web.config;

import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig;
import crm.integration.test.db.config.DbTestConfig;
import crm.integration.test.web.config.WebTestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({
    DictionaryWebConfig.class,
    DbTestConfig.class,
    WebTestConfig.class,
    JAsyncDictionaryTestConfig.class
})
@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:dictionary/web/dictionary-web-test.properties")
public class DictionaryWebTestConfig {}
