package com.ss.jcrm.user.api.test

import com.ss.jcrm.integration.test.db.config.TestHelper
import com.ss.jcrm.security.AccessRole
import com.ss.jcrm.user.api.EmailConfirmation
import com.ss.jcrm.user.api.Organization
import com.ss.jcrm.user.api.User
import com.ss.jcrm.user.api.UserGroup

interface UserTestHelper extends TestHelper {

    Organization newOrg()
    
    User newUser();
    
    User newUser(String name);
    
    User newUser(String name, AccessRole... roles);
    
    User newUser(String name, Organization organization);
    
    User newUser(String name, Organization organization, AccessRole... roles);
    
    User newUser(String email, String firstName, String secondName, String thirdName, Organization organization);
    
    User newUser(String name, String phoneNumber)
    
    User newUser(String name, String phoneNumber, String password);
    
    EmailConfirmation newEmailConfirmation();

    String nextUId()
    
    String nextEmail()
    
    Set<AccessRole> onlyOrgAdminRole();
    
    UserGroup newGroup(Organization organization);
}
