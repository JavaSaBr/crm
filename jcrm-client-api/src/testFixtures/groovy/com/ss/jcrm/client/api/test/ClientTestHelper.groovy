package com.ss.jcrm.client.api.test

import com.ss.jcrm.client.api.SimpleClient
import com.ss.jcrm.integration.test.db.config.TestHelper
import com.ss.jcrm.user.api.User

interface ClientTestHelper extends TestHelper {
    
    SimpleClient newSimpleClient()
    
    SimpleClient newSimpleClient(User assigner)
    
    String nextName();
}
