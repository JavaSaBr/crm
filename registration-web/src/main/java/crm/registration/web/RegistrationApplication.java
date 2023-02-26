package crm.registration.web;

import crm.registration.web.config.RegistrationWebConfig;
import crm.base.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(RegistrationWebConfig.class)
@Configuration(proxyBeanMethods = false)
public class RegistrationApplication extends BaseWebApplication {

  public static void main(@NotNull String[] args) {
    SpringApplication.run(RegistrationApplication.class, args);
  }
}
