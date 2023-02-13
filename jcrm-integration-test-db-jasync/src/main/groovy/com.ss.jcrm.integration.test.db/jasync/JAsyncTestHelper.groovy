package com.ss.jcrm.integration.test.db.jasync

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool

import java.util.concurrent.atomic.AtomicInteger

class JAsyncTestHelper {
  
  protected final ConnectionPool<? extends ConcreteConnection> connectionPool
  protected final String schema
  protected final AtomicInteger idGenerator = new AtomicInteger(0);
  
  JAsyncTestHelper(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema) {
    this.connectionPool = connectionPool
    this.schema = schema
  }
  
  String nextId(String prefix, String postfix) {
    return "$prefix${idGenerator.incrementAndGet()}$postfix";
  }
}
