package crm.client.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.client.api.ClientDbTestHelper;
import crm.client.api.dao.SimpleClientDao;
import crm.client.jasync.helper.JAsyncClientDbTestHelper;
import crm.user.api.UserDbTestHelper;
import crm.user.jasync.config.JAsyncUserTestConfig;
import integration.test.db.model.DbTestPrefix;
import integration.test.db.model.DbTestSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    JAsyncClientConfig.class,
    JAsyncUserTestConfig.class
})
@Configuration(proxyBeanMethods = false)
public class JAsyncClientTestConfig {

  @Bean
  DbTestPrefix clientDbTestPrefix() {
    return new DbTestPrefix("client.db", "ClientJAsync", DbTestSchema.CLIENT);
  }

  @Bean
  ClientDbTestHelper clientDbTestHelper(
      @Value("${user.db.schema}") String clientSchema,
      ConnectionPool<? extends ConcreteConnection> clientConnectionPool,
      UserDbTestHelper userTestHelper,
      SimpleClientDao simpleClientDao) {
    return new JAsyncClientDbTestHelper(clientConnectionPool, clientSchema, userTestHelper, simpleClientDao);
  }
}
