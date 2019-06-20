package com.ss.jcrm.jasync.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.rlib.common.util.ObjectUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@AllArgsConstructor
public abstract class AbstractJAsyncDao<T extends Entity> implements Dao<T> {

    protected final ConnectionPool<? extends ConcreteConnection> connectionPool;

    @Override
    public @Nullable T findById(long id) {
        return findByIdAsync(id).join();
    }

    @Override
    public @NotNull T requireById(long id) {
        return ObjectUtils.notNull(findById(id), id,
            value -> new ObjectNotFoundDaoException("Can't find an entity with the id " + value));
    }

    protected @NotNull CompletableFuture<Boolean> existBy(@NotNull String query, @NotNull Object value) {
        return connectionPool.sendPreparedStatement(query, List.of(value))
            .thenApply(queryResult -> !queryResult.getRows().isEmpty());
    }

    protected boolean deleteByLong(@NotNull String query, long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            statement.setLong(1, id);
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected <D extends Dao<T>> @Nullable T findByStringString(
        @NotNull String query,
        @NotNull String firstValue,
        @NotNull String secondValue,
        @NotNull JdbcConverter<D, T> converter
    ) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, firstValue);
            statement.setString(2, secondValue);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return converter.convert((D) this, rs);
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return null;
    }

    protected <D extends Dao<T>> @Nullable T findByString(
        @NotNull String query,
        @NotNull String value,
        @NotNull JdbcConverter<D, T> converter
    ) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, value);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return converter.convert((D) this, rs);
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return null;
    }

    protected <D extends Dao<T>> @Nullable T findByLong(
        @NotNull String query,
        long id,
        @NotNull JdbcConverter<D, T> converter
    ) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return converter.convert((D) this, rs);
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return null;
    }

    protected <D extends Dao<T>> @NotNull List<T> findAll(
        @NotNull String query,
        @NotNull JdbcConverter<D, T> converter
    ) {

        var result = new ArrayList<T>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(converter.convert((D) this, rs));
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    protected <D extends Dao<T>, A> @NotNull List<T> findAll(
        @NotNull String query,
        @NotNull A attachment,
        @NotNull JdbcBiConverter<D, A, T> converter
    ) {

        var result = new ArrayList<T>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(converter.convert((D) this, attachment, rs));
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    protected <D extends Dao<T>> @NotNull List<T> findAllByLong(
        @NotNull String query,
        long value,
        @NotNull JdbcConverter<D, T> converter
    ) {
        return findAllByLong(query, value, converter, ArrayList::new);
    }

    protected <D extends Dao<T>, C extends Collection<T>> @NotNull C findAllByLong(
        @NotNull String query,
        long value,
        @NotNull JdbcConverter<D, T> converter,
        @NotNull Supplier<C> collectionFactory
    ) {

        var result = collectionFactory.get();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            statement.setLong(1, value);

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(converter.convert((D) this, rs));
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }


}
