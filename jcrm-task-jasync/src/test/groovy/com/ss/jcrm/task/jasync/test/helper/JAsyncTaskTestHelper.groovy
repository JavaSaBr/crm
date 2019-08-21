package com.ss.jcrm.task.jasync.test.helper

import com.ss.jcrm.task.api.SimpleContact
import com.ss.jcrm.task.api.dao.SimpleContactDao
import com.ss.jcrm.task.api.test.TaskTestHelper
import com.ss.jcrm.user.api.test.UserTestHelper

class JAsyncTaskTestHelper implements TaskTestHelper {
    
    private final UserTestHelper userTestHelper
    private final SimpleContactDao simpleContactDao
    
    JAsyncTaskTestHelper(UserTestHelper userTestHelper, SimpleContactDao simpleContactDao) {
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
