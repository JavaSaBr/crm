package com.ss.jcrm.dictionary.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultCountry;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.rlib.common.util.array.Array;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JAsyncCountryDao extends AbstractDictionaryDao<Country> implements CountryDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"${schema}\".\"country\" where \"name\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"${schema}\".\"country\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"${schema}\".\"country\"";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"${schema}\".\"country\" (\"name\", \"flag_code\"," +
        " \"phone_code\") values (?, ?, ?) RETURNING id";

    private final String querySelectByName;
    private final String querySelectById;
    private final String querySelectAll;
    private final String queryInsert;

    public JAsyncCountryDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectAll = Q_SELECT_ALL.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.querySelectByName = Q_SELECT_BY_NAME.replace("${schema}", schema);
    }

    @Override
    public @NotNull Country create(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode) {
        return JAsyncUtils.unwrapJoin(createAsync(name, flagCode, phoneCode));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Country> createAsync(
        @NotNull String name,
        @NotNull String flagCode,
        @NotNull String phoneCode
    ) {

        return connectionPool.sendPreparedStatement(queryInsert, List.of(name, flagCode, phoneCode))
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();
                var id = notNull(rset.get(0).getLong(0));

                return new DefaultCountry(name, flagCode, phoneCode, id);
            });
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Array<Country>> findAllAsync() {
        return findAll(Country.class, querySelectAll, JAsyncCountryDao::toCountry);
    }

    @Override
    public @NotNull CompletableFuture<Country> findByIdAsync(long id) {
        return findBy(querySelectById, id, JAsyncCountryDao::toCountry);
    }

    @Override
    public @NotNull CompletableFuture<Country> findByNameAsync(@NotNull String name) {
        return findBy(querySelectByName, name, JAsyncCountryDao::toCountry);
    }

    private @NotNull DefaultCountry toCountry(@NotNull RowData data) {
        return new DefaultCountry(
            notNull(data.getString(1)),
            notNull(data.getString(2)),
            notNull(data.getString(3)),
            notNull(data.getLong(0))
        );
    }
}
