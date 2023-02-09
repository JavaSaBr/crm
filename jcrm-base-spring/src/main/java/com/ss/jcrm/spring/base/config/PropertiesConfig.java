package com.ss.jcrm.spring.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration(proxyBeanMethods = false)
public class PropertiesConfig {

  public static final String BEAN_EXTRA_PROPS = "extraProperties";
  public static final String BEAN_FINAL_PROPS = "finalProperties";

  @Bean(BEAN_EXTRA_PROPS)
  Void extraProperties() {
    return null;
  }

  @Bean
  @DependsOn(BEAN_EXTRA_PROPS)
  Void finalProperties() {
    return null;
  }
}
