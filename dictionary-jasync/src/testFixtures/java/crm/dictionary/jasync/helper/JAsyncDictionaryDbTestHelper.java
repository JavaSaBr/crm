package crm.dictionary.jasync.helper;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.dictionary.api.Country;
import crm.dictionary.api.DictionaryDbTestHelper;
import crm.dictionary.api.dao.CountryDao;
import crm.integration.test.jasync.JAsyncTestHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class JAsyncDictionaryDbTestHelper extends JAsyncTestHelper implements DictionaryDbTestHelper {

  private final CountryDao countryDao;

  public JAsyncDictionaryDbTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      CountryDao countryDao,
      String schema) {
    super(connectionPool, schema);
    this.countryDao = countryDao;
  }

  @Override
  public Country newCountry() {
    return newCountry(nextCountryName(), "none", "none");
  }

  @Override
  public Country newCountry(String name) {
    return newCountry(name, "none", "none");
  }

  @Override
  public Country newCountry(String name, String flagCode, String phoneCode) {
    return countryDao
        .create(name, flagCode, phoneCode)
        .block();
  }

  @Override
  public String nextCountryName() {
    return nextId("country_", "");
  }

  @Override
  public String nextIndustryName() {
    return nextId("industry_", "");
  }
}