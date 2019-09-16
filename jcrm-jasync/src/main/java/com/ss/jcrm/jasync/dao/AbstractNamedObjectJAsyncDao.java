package com.ss.jcrm.jasync.dao;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.NamedObjectDao;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractNamedObjectJAsyncDao<T extends NamedUniqEntity> extends AbstractJAsyncDao<T> implements
    NamedObjectDao<T> {

    public AbstractNamedObjectJAsyncDao(@NotNull ConnectionPool<? extends ConcreteConnection> connectionPool) {
        super(connectionPool);
    }
}
