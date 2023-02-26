package crm.mail.config;

import com.ss.rlib.mail.sender.MailSenderConfig;
import com.ss.rlib.testcontainers.FakeSMTPTestContainer;
import crm.mail.service.impl.JavaxMailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import(MailConfig.class)
@PropertySource("classpath:mail/mail-test.properties")
@Configuration(proxyBeanMethods = false)
public class MailTestConfig {

  @Bean
  FakeSMTPTestContainer fakeSMTPContainer() {

    var container = new FakeSMTPTestContainer();
    container.start();
    container.waitForReadyState();

    return container;
  }

  @Bean
  JavaxMailService mailService(FakeSMTPTestContainer fakeSMTPContainer) {

    var host = fakeSMTPContainer.getHost();
    var port = fakeSMTPContainer.getSmtpPort();
    var username = fakeSMTPContainer.getSmtpUser();
    var password = fakeSMTPContainer.getSmtpPassword();
    var smtpFrom = "test@test.test";

    var config = MailSenderConfig
        .builder()
        .enableTtls(false)
        .port(port)
        .host(host)
        .useAuth(true)
        .username(username)
        .password(password)
        .from(smtpFrom)
        .build();

    return new JavaxMailService(config);
  }
}
