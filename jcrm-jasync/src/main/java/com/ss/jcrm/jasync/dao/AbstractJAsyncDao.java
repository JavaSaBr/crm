package com.ss.jcrm.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static com.ss.rlib.common.util.array.ArrayCollectors.toArray;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.UniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.jasync.function.JAsyncBiConverter;
import com.ss.jcrm.jasync.function.JAsyncConverter;
import com.ss.jcrm.jasync.function.JAsyncCreationCallback;
import com.ss.jcrm.jasync.function.JAsyncLazyConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionException;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractJAsyncDao<T extends UniqEntity> implements Dao<T> {

    protected static final String VAR_ID_LIST = "${id-list}";

    private static final String[] ID_LIST_CACHE;

    static {

        var cacheSize = 100;
        var cache = new String[cacheSize];

        for (int i = 0; i < cacheSize; i++) {
            cache[i] = JAsyncUtils.buildQueryIdList(i);
        }

        ID_LIST_CACHE = cache;
    }

    @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool;
    @NotNull String schema;
    @NotNull String fieldList;

    @Deprecated
    protected AbstractJAsyncDao(@NotNull ConnectionPool<? extends ConcreteConnection> connectionPool) {
        this.connectionPool = connectionPool;
        this.schema = null;
        this.fieldList = null;
    }

    protected AbstractJAsyncDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull String fieldList
    ) {
        this.connectionPool = connectionPool;
        this.schema = schema;
        this.fieldList = fieldList.trim();
    }

    protected @NotNull String prepareQuery(@NotNull String query) {
        return query
            .replace("${schema}", schema)
            .replace("${field-list}", fieldList);
    }

    protected @NotNull String injectIdList(@NotNull String query, long @NotNull [] ids) {

        if (ID_LIST_CACHE.length > ids.length) {
            return query.replace(VAR_ID_LIST, ID_LIST_CACHE[ids.length]);
        }

        return query.replace(VAR_ID_LIST, JAsyncUtils.buildQueryIdList(ids));
    }

    protected abstract @NotNull Class<T> getEntityType();

    @Override
    public @NotNull Mono<@NotNull T> requireById(long id) {
        return findById(id)
            .switchIfEmpty(Mono.error(() -> new ObjectNotFoundDaoException("Can't find an entity with the id " + id)));
    }

    protected @NotNull Mono<Boolean> exist(@NotNull String query, @NotNull List<?> args) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .thenApply(queryResult -> !queryResult.getRows().isEmpty()));
    }

    protected @NotNull Mono<@NotNull T> insert(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncCreationCallback<T> callback
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();
                var id = notNull(rset.get(0).getLong(0));

                return callback.handle(id);
            }))
            .onErrorResume(
                CompletionException.class::isInstance,
                throwable -> Mono.error(throwable.getCause())
            );
    }

    protected @NotNull Mono<T> update(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull T entity
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                if (entity instanceof VersionedUniqEntity versioned) {

                    if (queryResult.getRowsAffected() < 1) {
                        throw new NotActualObjectDaoException(
                            "The object's version " + versioned.version() + " is outdated.");
                    }

                    versioned.version(versioned.version() + 1);
                }

                return entity;
            }));
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull T> select(
        @NotNull String query,
        @NotNull Object arg,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return select(query, List.of(arg), converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull T> select(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return null;
                } else {
                    return converter.convert((D) this, rset.get(0));
                }
            }));
    }

    protected @NotNull Mono<@NotNull Long> count(
        @NotNull String query,
        @NotNull Object arg
    ) {
        return count(query, List.of(arg));
    }

    protected @NotNull Mono<@NotNull Long> count(
        @NotNull String query,
        @NotNull List<?> args
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return 0L;
                } else {
                    return rset.get(0).getLong(0);
                }
            }));
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull T> selectAsync(
        @NotNull String query,
        @NotNull Object arg,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return selectAsync(query, List.of(arg), converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull T> selectAsync(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return null;
                } else {
                    return rset.get(0);
                }
            }))
            .flatMap(data -> converter.convert((D) this, data));
    }

    protected @NotNull Mono<Boolean> delete(
        @NotNull String query,
        @NotNull Object arg
    ) {
        return delete(query, List.of(arg));
    }

    protected @NotNull Mono<Boolean> delete(
        @NotNull String query,
        @NotNull List<?> args
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> queryResult.getRowsAffected() > 0));
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAll(
        @NotNull String query,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return selectAll(getEntityType(), query, converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return Mono.fromFuture(connectionPool.sendQuery(query)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(toArray(type));
            }));
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAll(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return selectAll(getEntityType(), query, args, converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return Mono.fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(toArray(type));
            }));
    }

    protected <D extends Dao<T>, A> @NotNull Mono<@NotNull Array<T>> selectAll(
        @NotNull String query,
        @NotNull A attachment,
        @NotNull JAsyncBiConverter<D, A, T> converter
    ) {
        return selectAll(getEntityType(), query, attachment, converter);
    }

    protected <D extends Dao<T>, A> @NotNull Mono<@NotNull Array<T>> selectAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull A attachment,
        @NotNull JAsyncBiConverter<D, A, T> converter
    ) {
        return Mono.fromFuture(connectionPool.sendQuery(query)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, attachment, data))
                    .collect(toArray(type));
            }));
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAllAsync(
        @NotNull String query,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return selectAllAsync(getEntityType(), query, converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAllAsync(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return Mono.<Array<Mono<T>>>fromFuture(connectionPool.sendQuery(query)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(toArray(Mono.class));
            }))
            .flatMapMany(Flux::concat)
            .collect(() -> ArrayFactory.newArray(type), Collection::add);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAllAsync(
        @NotNull String query,
        @NotNull Object arg,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return selectAllAsync(query, List.of(arg), converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAllAsync(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return selectAllAsync(getEntityType(), query, args, converter);
    }

    protected <D extends Dao<T>> @NotNull Mono<@NotNull Array<T>> selectAllAsync(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncLazyConverter<D, T> converter
    ) {
        return Mono.<Array<Mono<T>>>fromFuture(connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(toArray(Mono.class));
            }))
            .flatMapMany(Flux::concat)
            .collect(() -> ArrayFactory.newArray(type), Collection::add);
    }
}
