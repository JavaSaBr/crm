package crm.security.config;

import crm.security.service.PasswordService;
import crm.security.service.impl.DefaultPasswordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
    @PropertySource("classpath:security/security.properties"),
    @PropertySource(value = "classpath:security/security-${spring.profiles.active:default}.properties", ignoreResourceNotFound = true)
})
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

  @Bean
  @NotNull PasswordService passwordService(
      @Value("${security.password.key.iterations:10000}") int keyIterations,
      @Value("${security.password.key.length:256}") int keyLength) {
    return new DefaultPasswordService(keyIterations, keyLength);
  }
}
