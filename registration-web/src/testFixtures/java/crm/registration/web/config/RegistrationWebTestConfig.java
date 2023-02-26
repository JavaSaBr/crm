package crm.registration.web.config;

import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig;
import crm.integration.test.db.config.DbTestConfig;
import crm.mail.config.MailTestConfig;
import crm.user.jasync.config.JAsyncUserTestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({
    RegistrationWebConfig.class,
    DbTestConfig.class,
    JAsyncDictionaryTestConfig.class,
    JAsyncUserTestConfig.class,
    MailTestConfig.class
})
@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:registration/web/registration-web-test.properties")
public class RegistrationWebTestConfig {}
