package crm.dictionary.web;

import crm.dictionary.web.config.DictionaryWebConfig;
import crm.base.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(DictionaryWebConfig.class)
@Configuration(proxyBeanMethods = false)
public class DictionaryApplication extends BaseWebApplication {

  public static void main(@NotNull String[] args) {
    SpringApplication.run(DictionaryApplication.class, args);
  }
}
