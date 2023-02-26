package crm.client.api;

import crm.user.api.User;

public interface ClientDbTestHelper {

  SimpleClient newSimpleClient();

  SimpleClient newSimpleClient(User assigner);

  String nextClientName();
}
