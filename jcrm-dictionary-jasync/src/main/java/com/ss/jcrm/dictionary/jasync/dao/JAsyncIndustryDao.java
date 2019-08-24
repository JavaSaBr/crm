package com.ss.jcrm.dictionary.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dictionary.api.Industry;
import com.ss.jcrm.dictionary.api.dao.IndustryDao;
import com.ss.jcrm.dictionary.api.impl.DefaultIndustry;
import com.ss.jcrm.dictionary.jasync.AbstractDictionaryDao;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

public class JAsyncIndustryDao extends AbstractDictionaryDao<Industry> implements IndustryDao {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\" from \"${schema}\".\"industry\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\" from \"${schema}\".\"industry\" where \"id\" = ?";

    private static final String Q_SELECT_ALL = "select \"id\", \"name\" from \"${schema}\".\"industry\"";

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
        this.querySelectByName = Q_SELECT_BY_NAME.replace("${schema}", schema);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectAll = Q_SELECT_ALL.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
    }

    @Override
    public @NotNull Mono<@NotNull Industry> create(@NotNull String name) {
        return insert(
            queryInsert,
            List.of(name),
            id -> new DefaultIndustry(name, id)
        );
    }

    @Override
    public @NotNull Mono<@NotNull Array<Industry>> findAll() {
        return selectAll(Industry.class, querySelectAll, JAsyncIndustryDao::toIndustry);
    }

    @Override
    public @NotNull Mono<Industry> findById(long id) {
        return select(querySelectById, List.of(id), JAsyncIndustryDao::toIndustry);
    }

    @Override
    public @NotNull Mono<Industry> findByName(@NotNull String name) {
        return select(querySelectByName, List.of(name), JAsyncIndustryDao::toIndustry);
    }

    private @NotNull DefaultIndustry toIndustry(@NotNull RowData data) {
        return new DefaultIndustry(data.getString(1), notNull(data.getLong(0)));
    }
}
