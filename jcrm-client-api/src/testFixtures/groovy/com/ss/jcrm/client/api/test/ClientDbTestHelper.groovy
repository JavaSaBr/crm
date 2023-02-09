package com.ss.jcrm.client.api.test

import com.ss.jcrm.client.api.SimpleClient
import integration.test.db.DbTestHelper
import crm.user.api.User

interface ClientDbTestHelper extends DbTestHelper {
    
    SimpleClient newSimpleClient()
    
    SimpleClient newSimpleClient(User assigner)
    
    String nextName();
}
