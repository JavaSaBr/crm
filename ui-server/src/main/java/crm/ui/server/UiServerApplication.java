package crm.ui.server;

import crm.ui.server.config.UiServerConfig;
import crm.base.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(UiServerConfig.class)
@Configuration(proxyBeanMethods = false)
public class UiServerApplication extends BaseWebApplication {

  public static void main(@NotNull String[] args) {
    SpringApplication.run(UiServerApplication.class, args);
  }
}
