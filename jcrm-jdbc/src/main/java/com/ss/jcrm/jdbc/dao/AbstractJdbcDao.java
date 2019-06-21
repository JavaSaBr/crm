package com.ss.jcrm.jdbc.dao;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.jdbc.function.JdbcBiConverter;
import com.ss.jcrm.jdbc.function.JdbcConverter;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public abstract class AbstractJdbcDao<T extends Entity> implements Dao<T> {

    protected final DataSource dataSource;
    protected final Executor fastDbTaskExecutor;
    protected final Executor slowDbTaskExecutor;

    public AbstractJdbcDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        this.dataSource = dataSource;
        this.fastDbTaskExecutor = fastDbTaskExecutor;
        this.slowDbTaskExecutor = slowDbTaskExecutor;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable T> findByIdAsync(long id) {
        return supplyAsync(() -> findById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull T requireById(long id) {
        return ObjectUtils.notNull(findById(id), id,
            value -> new ObjectNotFoundDaoException("Can't find an entity with the id " + value));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull T> requireByIdAsync(long id) {
        return supplyAsync(() -> requireById(id), fastDbTaskExecutor);
    }

    protected boolean existByString(@NotNull String query, @NotNull String value) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, value);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return false;
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

    protected <D extends Dao<T>> @NotNull Array<T> findAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull JdbcConverter<D, T> converter
    ) {

        var result = ArrayFactory.newArray(type);

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

    protected <D extends Dao<T>, A> @NotNull Array<T> findAll(
        @NotNull Class<T> type,
        @NotNull String query,
        @NotNull A attachment,
        @NotNull JdbcBiConverter<D, A, T> converter
    ) {

        var result = ArrayFactory.newArray(type);

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
