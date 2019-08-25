package com.ss.jcrm.client.api.test

import com.ss.jcrm.client.api.SimpleContact

interface ClientTestHelper {
    
    SimpleContact newSimpleContact()
    
    String nextName();
}
