package com.ss.jcrm.client.jasync.test.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.client.api.SimpleContact
import com.ss.jcrm.client.api.dao.SimpleContactDao
import com.ss.jcrm.client.api.test.ClientTestHelper
import com.ss.jcrm.client.jasync.test.JAsyncClientSpecification
import com.ss.jcrm.integration.test.db.jasync.JAsyncTestHelper
import com.ss.jcrm.user.api.User
import com.ss.jcrm.user.api.test.UserTestHelper

class JAsyncClientTestHelper extends JAsyncTestHelper implements ClientTestHelper {
    
    private final UserTestHelper userTestHelper
    private final SimpleContactDao simpleContactDao
    
    JAsyncClientTestHelper(
        ConnectionPool<? extends ConcreteConnection> connectionPool,
        String schema,
        UserTestHelper userTestHelper,
        SimpleContactDao simpleContactDao
    ) {
        super(connectionPool, schema)
        this.userTestHelper = userTestHelper
        this.simpleContactDao = simpleContactDao
    }
    
    @Override
    SimpleContact newSimpleContact() {
        def user = userTestHelper.newUser()
        return simpleContactDao.create(
            user,
            user.organization,
            nextName(),
            nextName(),
        ).block()
    }
    
    @Override
    SimpleContact newSimpleContact(User user) {
        return simpleContactDao.create(
            user,
            user.organization,
            nextName(),
            nextName(),
        ).block()
    }
    
    @Override
    String nextName() {
        def baseName = "Name" + System.currentTimeMillis()
        return baseName.substring(0, Math.min(baseName.length() - 1, 44))
    }
    
    @Override
    void clearAllData() {
        JAsyncClientSpecification.clearAllTables(connectionPool, schema)
    }
}
