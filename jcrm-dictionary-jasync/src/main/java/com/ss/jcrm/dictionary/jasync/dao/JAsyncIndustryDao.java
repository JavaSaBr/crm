package com.ss.jcrm.dictionary.jasync.dao;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.Industry;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultIndustry;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.array.Array;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JAsyncIndustryDao extends AbstractDictionaryDao<Industry> implements IndustryDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\" from \"${schema}\".\"industry\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\" from \"${schema}\".\"industry\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\" from \"${schema}\".\"industry\"";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"${schema}\".\"industry\" (\"name\") values (?) RETURNING id";

    private final String querySelectByName;
    private final String querySelectById;
    private final String querySelectAll;
    private final String queryInsert;

    public JAsyncIndustryDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool);
        querySelectByName = Q_SELECT_BY_NAME.replace("${schema}", schema);
        querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        querySelectAll = Q_SELECT_ALL.replace("${schema}", schema);
        queryInsert = Q_INSERT.replace("${schema}", schema);
    }

    @Override
    public @NotNull Industry create(@NotNull String name) {
        return createAsync(name).join();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Industry> createAsync(@NotNull String name) {

        return connectionPool.sendPreparedStatement(queryInsert, List.of(name))
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();
                var id = ObjectUtils.notNull(rset.get(0).getLong(0));

                return new DefaultIndustry(name, id);
            });
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Array<Industry>> findAllAsync() {
        return findAll(Industry.class, querySelectAll, JAsyncIndustryDao::toIndustry);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Industry> findByIdAsync(long id) {
        return findBy(querySelectById, id, JAsyncIndustryDao::toIndustry);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Industry> findByNameAsync(@NotNull String name) {
        return findBy(querySelectByName, name, JAsyncIndustryDao::toIndustry);
    }

    private @NotNull DefaultIndustry toIndustry(@NotNull RowData data) {
        return new DefaultIndustry(data.getString(2), ObjectUtils.notNull(data.getLong(1)));
    }
}
