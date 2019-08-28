package com.ss.jcrm.client.api.test

import com.ss.jcrm.client.api.SimpleContact
import com.ss.jcrm.integration.test.db.config.TestHelper
import com.ss.jcrm.user.api.User

interface ClientTestHelper extends TestHelper {
    
    SimpleContact newSimpleContact()
    
    SimpleContact newSimpleContact(User user)
    
    String nextName();
}
