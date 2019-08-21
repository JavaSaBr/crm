package com.ss.jcrm.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static com.ss.rlib.common.util.array.ArrayCollectors.toArray;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import com.ss.jcrm.dao.VersionedEntity;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.jasync.function.*;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.array.Array;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class AbstractJAsyncDao<T extends Entity> implements Dao<T> {

    protected final ConnectionPool<? extends ConcreteConnection> connectionPool;

    @Override
    public @Nullable T findById(long id) {
        return JAsyncUtils.unwrapJoin(findByIdAsync(id));
    }

    @Override
    public @NotNull T requireById(long id) {
        return JAsyncUtils.unwrapJoin(requireByIdAsync(id));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull T> requireByIdAsync(long id) {
        return findByIdAsync(id)
            .thenApply(entity -> ObjectUtils.notNull(entity, id,
                value -> new ObjectNotFoundDaoException("Can't find an entity with the id " + value)));
    }

    protected @NotNull CompletableFuture<Boolean> existBy(@NotNull String query, @NotNull Object value) {
        return connectionPool.sendPreparedStatement(query, List.of(value))
            .thenApply(queryResult -> !queryResult.getRows().isEmpty());
    }

    protected @NotNull CompletableFuture<Boolean> deleteBy(@NotNull String query, @NotNull Object value) {
        return connectionPool.sendPreparedStatement(query, List.of(value))
            .thenApply(queryResult -> queryResult.getRows().size() == 1);
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@NotNull T> insert(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncCreationCallback<D, T> callback
    ) {

        return connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();
                var id = notNull(rset.get(0).getLong(0));

                return callback.handle((D) this, id);
            });
    }

    protected @NotNull CompletableFuture<Void> update(
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull T entity
    ) {

        return connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                if (entity instanceof VersionedEntity) {

                    var versioned = (VersionedEntity) entity;

                    if (queryResult.getRowsAffected() < 1) {
                        throw new NotActualObjectDaoException(
                            "The object's version " + versioned.getVersion() + " is outdated.");
                    }

                    versioned.setVersion(versioned.getVersion() + 1);
                }

                return null;
            });
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@Nullable T> findBy(
        @NotNull String query,
        @NotNull Object firstValue,
        @NotNull Object secondValue,
        @NotNull JAsyncConverter<D, T> converter
    ) {

        return connectionPool.sendPreparedStatement(query, List.of(firstValue, secondValue))
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return null;
                } else {
                    return converter.convert((D) this, rset.get(0));
                }
            });
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@Nullable T> findBy(
        @NotNull String query,
        @NotNull Object value,
        @NotNull JAsyncConverter<D, T> converter
    ) {

        return connectionPool.sendPreparedStatement(query, List.of(value))
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return null;
                } else {
                    return converter.convert((D) this, rset.get(0));
                }
            });
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@Nullable T> findByAndCompose(
        @NotNull String query,
        @NotNull Object value,
        @NotNull JAsyncComposeConverter<D, T> converter
    ) {

        return connectionPool.sendPreparedStatement(query, List.of(value))
            .handle(JAsyncUtils.handleException())
            .thenCompose(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return CompletableFuture.completedFuture(null);
                } else {
                    return converter.convert((D) this, rset.get(0));
                }
            });
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@NotNull Array<T>> findAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull JAsyncConverter<D, T> converter
    ) {

        return connectionPool.sendQuery(query)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(toArray(type));
            });
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@NotNull Array<T>> findAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull List<?> args,
        @NotNull JAsyncConverter<D, T> converter
    ) {

        return connectionPool.sendPreparedStatement(query, args)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(toArray(type));
            });
    }

    protected <D extends Dao<T>, A> @NotNull CompletableFuture<@NotNull Array<T>> findAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull A attachment,
        @NotNull JAsyncBiConverter<D, A, T> converter
    ) {

        return connectionPool.sendQuery(query)
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return Array.empty();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, attachment, data))
                    .collect(toArray(type));
            });
    }

    protected <D extends Dao<T>> @NotNull CompletableFuture<@NotNull List<T>> findAllByLong(
        @NotNull String query,
        @NotNull Object value,
        @NotNull JAsyncConverter<D, T> converter
    ) {
        return findAllBy(query, value, converter, ArrayList::new);
    }

    protected <D extends Dao<T>, C extends Collection<T>> @NotNull CompletableFuture<@NotNull C> findAllBy(
        @NotNull String query,
        @NotNull Object value,
        @NotNull JAsyncConverter<D, T> converter,
        @NotNull Supplier<C> collectionFactory
    ) {

        return connectionPool.sendPreparedStatement(query, List.of(value))
            .handle(JAsyncUtils.handleException())
            .thenApply(queryResult -> {

                var rset = queryResult.getRows();

                if (rset.isEmpty()) {
                    return collectionFactory.get();
                }

                return rset.stream()
                    .map(data -> converter.convert((D) this, data))
                    .collect(Collectors.toCollection(collectionFactory));
            });
    }
}
