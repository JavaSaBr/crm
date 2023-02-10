package com.ss.jcrm.client.jasync.test.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import crm.client.api.SimpleClient
import crm.client.api.dao.SimpleClientDao
import com.ss.jcrm.client.jasync.test.JAsyncClientSpecification
import com.ss.jcrm.integration.test.db.jasync.JAsyncTestHelper
import crm.client.api.ClientDbTestHelper
import crm.user.api.User
import crm.user.api.UserDbTestHelper

class JAsyncClientDbTestHelper extends JAsyncTestHelper implements ClientDbTestHelper {
    
    private final UserDbTestHelper userTestHelper
    private final SimpleClientDao simpleContactDao
  
  JAsyncClientDbTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema,
      UserDbTestHelper userTestHelper,
      SimpleClientDao simpleContactDao
    ) {
        super(connectionPool, schema)
        this.userTestHelper = userTestHelper
        this.simpleContactDao = simpleContactDao
    }
    
    @Override
    SimpleClient newSimpleClient() {
        def user = userTestHelper.newUser()
        return simpleContactDao.create(
            user,
            user.organization,
            nextName(),
            nextName(),
            nextName(),
        ).block()
    }
    
    @Override
    SimpleClient newSimpleClient(User assigner) {
        return simpleContactDao.create(
            assigner,
            assigner.organization,
            nextName(),
            nextName(),
            nextName()
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
