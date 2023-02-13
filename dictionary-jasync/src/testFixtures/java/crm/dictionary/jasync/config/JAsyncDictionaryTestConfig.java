package crm.dictionary.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.dictionary.api.DictionaryDbTestHelper;
import crm.dictionary.api.dao.CountryDao;
import crm.dictionary.jasync.helper.JAsyncDictionaryDbTestHelper;
import crm.jasync.config.JAsyncTestConfig;
import integration.test.db.config.DbTestConfig;
import integration.test.db.model.DbTestPrefix;
import integration.test.db.model.DbTestSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    JAsyncDictionaryConfig.class,
    DbTestConfig.class,
    JAsyncTestConfig.class
})
@Configuration(proxyBeanMethods = false)
public class JAsyncDictionaryTestConfig {

  @Bean
  DbTestPrefix dictionaryDbTestPrefix() {
    return new DbTestPrefix("dictionary.db", "DictionaryJAsync", DbTestSchema.DICTIONARY);
  }

  @Bean
  DictionaryDbTestHelper dictionaryTestHelper(
      ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool,
      CountryDao countryDao,
      @Value("${dictionary.db.schema}") String dictionarySchema) {
    return new JAsyncDictionaryDbTestHelper(dictionaryConnectionPool, countryDao, dictionarySchema);
  }
}
