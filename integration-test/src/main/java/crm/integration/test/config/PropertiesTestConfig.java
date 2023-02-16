package crm.integration.test.config;

import crm.base.spring.config.PropertiesConfig;
import crm.integration.test.model.PropertiesTestProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

@Import({
    PropertiesConfig.class
})
@Configuration(proxyBeanMethods = false)
class PropertiesTestConfig {

  @Bean(PropertiesConfig.BEAN_EXTRA_PROPS)
  Void extraProperties(
      @NotNull ConfigurableEnvironment environment,
      @NotNull List<PropertiesTestProvider> providers) {

    var propertySources = environment.getPropertySources();

    providers.forEach(provider -> propertySources.addFirst(new MapPropertySource(
        provider.getClass().getName(), provider.provide())));

    return null;
  }
}

