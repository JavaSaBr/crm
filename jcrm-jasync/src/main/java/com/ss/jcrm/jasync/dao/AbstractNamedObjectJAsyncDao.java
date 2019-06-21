package com.ss.jcrm.jasync.dao;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.NamedEntity;
import com.ss.jcrm.dao.NamedObjectDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNamedObjectJAsyncDao<T extends NamedEntity> extends AbstractJAsyncDao<T> implements
    NamedObjectDao<T> {

    public AbstractNamedObjectJAsyncDao(@NotNull ConnectionPool<? extends ConcreteConnection> connectionPool) {
        super(connectionPool);
    }

    @Override
    public @Nullable T findByName(@NotNull String name) {
        return findByNameAsync(name).join();
    }
}
