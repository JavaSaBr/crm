package crm.client.jasync.helper;

import static crm.integration.test.util.TestUtils.waitForResult;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.integration.test.db.jasync.JAsyncTestHelper;
import crm.client.api.ClientDbTestHelper;
import crm.client.api.SimpleClient;
import crm.client.api.dao.SimpleClientDao;
import crm.user.api.User;
import crm.user.api.UserDbTestHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncClientDbTestHelper extends JAsyncTestHelper implements ClientDbTestHelper  {

  UserDbTestHelper userTestHelper;
  SimpleClientDao simpleClientDao;

  public JAsyncClientDbTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema,
      UserDbTestHelper userTestHelper,
      SimpleClientDao simpleClientDao) {
    super(connectionPool, schema);
    this.userTestHelper = userTestHelper;
    this.simpleClientDao = simpleClientDao;
  }

  @Override
  public SimpleClient newSimpleClient() {
    var user = userTestHelper.newUser();
    return waitForResult(simpleClientDao
        .create(user, user.organization(), nextClientName(), nextClientName(), nextClientName()));
  }

  @Override
  public SimpleClient newSimpleClient(User assigner) {
    return waitForResult(simpleClientDao
        .create(assigner, assigner.organization(), nextClientName(), nextClientName(), nextClientName()));
  }

  @Override
  public String nextClientName() {
    return nextId("client_", "");
  }

  @Override
  public void clearAllData() {}
}
