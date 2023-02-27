package crm.dictionary.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.base.spring.config.PropertiesConfig;
import crm.dao.config.DaoConfig;
import crm.dictionary.api.dao.CityDao;
import crm.dictionary.api.dao.CountryDao;
import crm.dictionary.api.dao.IndustryDao;
import crm.dictionary.jasync.dao.JAsyncCityDao;
import crm.dictionary.jasync.dao.JAsyncCountryDao;
import crm.dictionary.jasync.dao.JAsyncIndustryDao;
import crm.jasync.config.AbstractJAsyncConfig;
import crm.jasync.config.CommonJAsyncConfig;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@PropertySources({
    @PropertySource("classpath:dictionary/jasync/dictionary-jasync.properties"),
    @PropertySource(
        value = "classpath:dictionary/jasync/dictionary-jasync-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true)
})
@Import({
    CommonJAsyncConfig.class,
    PropertiesConfig.class
})
@Configuration(proxyBeanMethods = false)
public class JAsyncDictionaryConfig extends AbstractJAsyncConfig {

  public JAsyncDictionaryConfig(@NotNull ApplicationContext applicationContext) {
    super(applicationContext, "dictionary.db");
  }

  @Bean
  @DependsOn(PropertiesConfig.BEAN_FINAL_PROPS)
  @NotNull Flyway dictionaryFlyway() {
    return flyway();
  }

  @Bean
  @DependsOn({
      PropertiesConfig.BEAN_FINAL_PROPS,
      DaoConfig.BEAN_DB_READY
  })
  @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool() {
    return connectionPool();
  }

  @Bean
  @NotNull CountryDao countryDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool) {
    return new JAsyncCountryDao(dictionaryConnectionPool, schema());
  }

  @Bean
  @NotNull CityDao cityDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool,
      @NotNull CountryDao countryDao) {
    return new JAsyncCityDao(dictionaryConnectionPool, schema(), countryDao);
  }

  @Bean
  @NotNull IndustryDao industryDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool) {
    return new JAsyncIndustryDao(dictionaryConnectionPool, schema());
  }
}

