package com.ss.jcrm.integration.test.config

import com.ss.jcrm.integration.test.model.PropertiesTestProvider
import com.ss.jcrm.spring.base.config.PropertiesConfig
import org.jetbrains.annotations.NotNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

@Import([
    PropertiesConfig
])
@Configuration(proxyBeanMethods = false)
class PropertiesTestConfig {
  
  @Bean(PropertiesConfig.BEAN_EXTRA_PROPS)
  Void extraProperties(
      @NotNull ConfigurableEnvironment environment,
      @NotNull List<PropertiesTestProvider> providers) {
  
    def propertySources = environment.getPropertySources();
    
    providers.forEach(provider -> propertySources.addFirst(
        new MapPropertySource(provider.class.getName(), provider.provide())));
  
    return null;
  }
}
