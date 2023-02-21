package crm.user.api;

import crm.security.AccessRole;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import java.util.Set;

public interface UserDbTestHelper {

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

