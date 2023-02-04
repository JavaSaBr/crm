package com.ss.jcrm.integration.test.db.jasync

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool

class JAsyncTestHelper {
  
  protected final ConnectionPool<? extends ConcreteConnection> connectionPool
  protected final String schema
  
  JAsyncTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema) {
    this.connectionPool = connectionPool
    this.schema = schema
  }
}
