package crm.user.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.dao.CityDao;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import crm.base.spring.config.PropertiesConfig;
import jasync.config.AbstractJAsyncConfig;
import jasync.config.CommonJAsyncConfig;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.dao.MinimalUserDao;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import jcrm.dao.config.DaoConfig;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import crm.user.jasync.dao.JAsyncEmailConfirmationDao;
import crm.user.jasync.dao.JAsyncMinimalUserDao;
import crm.user.jasync.dao.JAsyncOrganizationDao;
import crm.user.jasync.dao.JAsyncUserDao;
import crm.user.jasync.dao.JAsyncUserGroupDao;

@PropertySources({
    @PropertySource("classpath:user/jasync/user-jasync.properties"),
    @PropertySource(value = "classpath:user/jasync/user-jasync-${spring.profiles.active:default}.properties", ignoreResourceNotFound = true)
})
@Import(CommonJAsyncConfig.class)
@Configuration(proxyBeanMethods = false)
public class JAsyncUserConfig extends AbstractJAsyncConfig {

  public JAsyncUserConfig(@NotNull ApplicationContext applicationContext) {
    super(applicationContext, "user.db");
  }

  @Bean
  @NotNull Flyway userFlyway() {
    return flyway();
  }

  @Bean
  @DependsOn({
      PropertiesConfig.BEAN_FINAL_PROPS,
      DaoConfig.BEAN_DB_READY
  })
  @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool() {
    return connectionPool();
  }

  @Bean
  @NotNull UserDao userDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool,
      @NotNull OrganizationDao organizationDao,
      @NotNull UserGroupDao userGroupDao) {
    return new JAsyncUserDao(userConnectionPool, schema(), organizationDao, userGroupDao);
  }

  @Bean
  @NotNull MinimalUserDao minimalUserDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool) {
    return new JAsyncMinimalUserDao(userConnectionPool, schema());
  }

  @Bean
  @NotNull UserGroupDao userGroupDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool,
      @NotNull OrganizationDao organizationDao) {
    return new JAsyncUserGroupDao(userConnectionPool, schema(), organizationDao);
  }

  @Bean
  @NotNull OrganizationDao organizationDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool,
      @NotNull CityDao cityDao,
      @NotNull IndustryDao industryDao,
      @NotNull CountryDao countryDao) {
    return new JAsyncOrganizationDao(userConnectionPool, schema(), cityDao, industryDao, countryDao);
  }

  @Bean
  @NotNull EmailConfirmationDao emailConfirmationDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> userConnectionPool) {
    return new JAsyncEmailConfirmationDao(userConnectionPool, schema());
  }
}
