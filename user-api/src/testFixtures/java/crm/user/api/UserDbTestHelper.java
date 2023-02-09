package crm.user.api;

import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import integration.test.db.DbTestHelper;
import java.util.Set;

public interface UserDbTestHelper extends DbTestHelper {

  Organization newOrganization();

  User newUser();

  User newUser(String name);

  User newUser(String name, String password);

  User newUser(String name, AccessRole... roles);

  User newUser(String name, Organization organization);

  default User newUser(Organization organization) {
    return newUser(nextEmail(), organization);
  }

  User newUser(String name, Organization organization, AccessRole... roles);

  User newUser(String email, String firstName, String secondName, String thirdName, Organization organization);

  User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers);

  User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers, String password);

  UserGroup newGroup(Organization organization);

  UserGroup newGroup(String name, Organization organization);

  UserGroup newGroup(String name, Organization organization, Set<AccessRole> roles);

  EmailConfirmation newEmailConfirmation();

  String nextOrganizationName();

  String nextEmail();

  String nextGroupName();

  Set<AccessRole> onlyOrgAdminRole();
}
