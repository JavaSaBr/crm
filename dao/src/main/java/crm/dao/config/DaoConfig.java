package crm.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DaoConfig {

  public static final String BEAN_DB_READY = "dbReady";

  @Bean(BEAN_DB_READY)
  Void dbReady() {
    return null;
  }
}
