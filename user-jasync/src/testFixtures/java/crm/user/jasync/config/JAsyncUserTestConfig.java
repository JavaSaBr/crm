package crm.user.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.security.config.SecurityConfig;
import crm.security.service.PasswordService;
import crm.dictionary.api.DictionaryDbTestHelper;
import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig;
import crm.integration.test.db.config.DbTestConfig;
import crm.integration.test.db.model.DbTestPrefix;
import crm.integration.test.db.model.DbTestSchema;
import crm.user.api.UserDbTestHelper;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import crm.jasync.config.JAsyncTestConfig;
import crm.user.jasync.helper.JAsyncUserDbTestHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    JAsyncUserConfig.class,
    SecurityConfig.class,
    DbTestConfig.class,
    JAsyncTestConfig.class,
    JAsyncDictionaryTestConfig.class
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
