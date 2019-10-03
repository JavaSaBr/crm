package com.ss.jcrm.dictionary.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.dao.CountryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultCountry;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import com.ss.jcrm.jasync.function.JAsyncConverter;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

public class JAsyncCountryDao extends AbstractDictionaryDao<Country> implements CountryDao {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"${schema}\".\"country\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"${schema}\".\"country\" where \"id\" = ?";

    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " from \"${schema}\".\"country\"";

    private static final String Q_INSERT = "insert into \"${schema}\".\"country\" (\"name\", \"flag_code\"," +
        " \"phone_code\") values (?, ?, ?) returning id";

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
    protected @NotNull Class<Country> getEntityType() {
        return Country.class;
    }

    @Override
    public @NotNull Mono<@NotNull Country> create(
        @NotNull String name,
        @NotNull String flagCode,
        @NotNull String phoneCode
    ) {
        return insert(
            queryInsert,
            List.of(name, flagCode, phoneCode),
            id -> new DefaultCountry(name, flagCode, phoneCode, id)
        );
    }

    @Override
    public @NotNull Mono<@NotNull Array<Country>> findAll() {
        return selectAll(querySelectAll, converter());
    }

    @Override
    public @NotNull Mono<Country> findById(long id) {
        return select(querySelectById, List.of(id), converter());
    }

    @Override
    public @NotNull Mono<Country> findByName(@NotNull String name) {
        return select(querySelectByName, List.of(name), converter());
    }

    private @NotNull JAsyncConverter<JAsyncCountryDao, Country> converter() {
        return JAsyncCountryDao::toCountry;
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
