package com.ss.jcrm.task.api.test

import com.ss.jcrm.task.api.SimpleContact

interface TaskTestHelper {
    
    SimpleContact newSimpleContact()
    
    String nextName();
}
