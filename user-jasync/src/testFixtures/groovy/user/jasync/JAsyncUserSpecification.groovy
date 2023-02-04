package user.jasync

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.DefaultSpecification
import crm.user.api.test.UserTestHelper
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import static com.ss.jcrm.integration.test.db.jasync.util.DbSpecificationUtils.clearTable

@ContextConfiguration(classes = JAsyncUserSpecificationConfig)
class JAsyncUserSpecification extends DefaultSpecification {
  
  static final String TABLE_ORGANIZATION = "organization"
  static final String TABLE_USER_GROUP = "user_group"
  static final String TABLE_USER = "user"
  static final String TABLE_EMAIL_CONFIRMATION = "email_confirmation"
  
  def static clearAllTables(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema) {
    clearTable(connectionPool, schema, TABLE_USER, TABLE_USER_GROUP, TABLE_ORGANIZATION, TABLE_EMAIL_CONFIRMATION)
  }
  
  @Autowired
  DictionaryTestHelper dictionaryTestHelper
  
  @Autowired
  UserTestHelper userTestHelper
  
  @Autowired
  List<? extends Flyway> flyways
  
  def setup() {
    userTestHelper.clearAllData()
    dictionaryTestHelper.clearAllData()
  }
}
