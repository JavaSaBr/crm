package com.ss.jcrm.client.jasync.test.helper

import com.ss.jcrm.client.api.SimpleContact
import com.ss.jcrm.client.api.dao.SimpleContactDao
import com.ss.jcrm.client.api.test.ClientTestHelper
import com.ss.jcrm.user.api.test.UserTestHelper

class JAsyncClientTestHelper implements ClientTestHelper {
    
    private final UserTestHelper userTestHelper
    private final SimpleContactDao simpleContactDao
    
    JAsyncClientTestHelper(UserTestHelper userTestHelper, SimpleContactDao simpleContactDao) {
        this.userTestHelper = userTestHelper
        this.simpleContactDao = simpleContactDao
    }
    
    @Override
    SimpleContact newSimpleContact() {
    
        def org = userTestHelper.newOrg()
        
        return simpleContactDao.create(
            org,
            nextName(),
            nextName(),
            nextName(),
        ).block()
    }
    
    @Override
    String nextName() {
        def baseName = "Name" + System.currentTimeMillis()
        return baseName.substring(0, Math.min(baseName.length() - 1, 44))
    }
}
