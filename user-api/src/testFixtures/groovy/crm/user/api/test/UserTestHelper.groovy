package crm.user.api.test

import com.ss.jcrm.integration.test.db.config.TestHelper
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.user.contact.api.Messenger
import com.ss.jcrm.user.contact.api.PhoneNumber
import crm.user.api.EmailConfirmation
import crm.user.api.Organization
import crm.user.api.User
import crm.user.api.UserGroup

interface UserTestHelper extends TestHelper {
  
  Organization newOrg()
  
  User newUser();
  
  User newUser(String name);
  
  User newUser(String name, String password);
  
  User newUser(String name, AccessRole... roles);
  
  User newUser(String name, Organization organization);
  
  User newUser(String name, Organization organization, AccessRole... roles);
  
  User newUser(String email, String firstName, String secondName, String thirdName, Organization organization);
  
  User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers)
  
  User newUser(String name, Set<PhoneNumber> phoneNumbers, Set<Messenger> messengers, String password);
  
  UserGroup newGroup(Organization organization);
  
  UserGroup newGroup(String name, Organization organization);
  
  UserGroup newGroup(String name, Organization organization, Set<AccessRole> roles);
  
  EmailConfirmation newEmailConfirmation();
  
  String nextUId()
  
  String nextEmail()
  
  Set<AccessRole> onlyOrgAdminRole();
}
