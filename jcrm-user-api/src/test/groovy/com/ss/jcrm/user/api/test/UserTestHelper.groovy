package com.ss.jcrm.user.api.test

import com.ss.jcrm.user.api.EmailConfirmation
import com.ss.jcrm.user.api.Organization
import com.ss.jcrm.user.api.User

interface UserTestHelper {

    Organization newOrg()
    
    User newUser();
    
    User newUser(String name, String phoneNumber, String password);
    
    EmailConfirmation newEmailConfirmation();
    
    void clearAllData()

    String nextUId()
    
    String nextEmail()
}
