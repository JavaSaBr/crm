package crm.user.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.test.DictionaryDbTestHelper;
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecificationConfig;
import com.ss.jcrm.security.config.SecurityConfig;
import com.ss.jcrm.security.service.PasswordService;
import crm.user.api.UserDbTestHelper;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import integration.test.db.config.DbTestConfig;
import integration.test.db.model.DbTestPrefix;
import integration.test.db.model.DbTestSchema;
import jcrm.jasync.config.JAsyncTestConfig;
import crm.user.jasync.helper.JAsyncUserDbTestHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    JAsyncUserConfig.class,
    DbTestConfig.class,
    JAsyncDictionarySpecificationConfig.class,
    SecurityConfig.class,
    JAsyncTestConfig.class
})
@Configuration(proxyBeanMethods = false)
public class JAsyncUserTestConfig {

  @Bean
  DbTestPrefix userDbTestPrefix() {
    return new DbTestPrefix("user.db", "UserJAsync", DbTestSchema.USER);
  }

  @Bean
  UserDbTestHelper userTestHelper(
      @Value("${user.db.schema}") String userSchema,
      ConnectionPool<? extends ConcreteConnection> userConnectionPool,
      UserDao userDao,
      UserGroupDao userRoleDao,
      OrganizationDao organizationDao,
      PasswordService passwordService,
      DictionaryDbTestHelper dictionaryTestHelper,
      EmailConfirmationDao emailConfirmationDao) {
    return new JAsyncUserDbTestHelper(
        userConnectionPool,
        userSchema,
        userDao,
        userRoleDao,
        organizationDao,
        passwordService,
        dictionaryTestHelper,
        emailConfirmationDao);
  }
}
