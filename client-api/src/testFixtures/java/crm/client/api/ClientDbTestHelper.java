package crm.client.api;

import crm.user.api.User;
import integration.test.db.DbTestHelper;

public interface ClientDbTestHelper extends DbTestHelper {

  SimpleClient newSimpleClient();

  SimpleClient newSimpleClient(User assigner);

  String nextName();
}
