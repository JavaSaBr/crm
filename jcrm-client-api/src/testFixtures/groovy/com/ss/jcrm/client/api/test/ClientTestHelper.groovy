package com.ss.jcrm.client.api.test

import com.ss.jcrm.client.api.SimpleClient
import com.ss.jcrm.integration.test.db.config.TestHelper
import crm.user.api.User

interface ClientTestHelper extends TestHelper {
    
    SimpleClient newSimpleClient()
    
    SimpleClient newSimpleClient(User assigner)
    
    String nextName();
}
