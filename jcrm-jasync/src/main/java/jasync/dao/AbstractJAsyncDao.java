package jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.UniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import jasync.function.JAsyncBiConverter;
import jasync.function.JAsyncConverter;
import jasync.function.JAsyncCreationCallback;
import jasync.function.JAsyncLazyConverter;
import jasync.util.JAsyncUtils;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  protected AbstractJAsyncDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
      @NotNull String schema,
      @NotNull String fieldList) {
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

  @Deprecated
  protected Class<T> getEntityType() {
    return null;
  };

  @Override
  public @NotNull Mono<@NotNull T> requireById(long id) {
    return findById(id)
        .switchIfEmpty(Mono.error(() -> new ObjectNotFoundDaoException("Can't find an entity with the id " + id)));
  }

  protected @NotNull Mono<Boolean> exist(@NotNull String query, @NotNull List<?> args) {
    return Mono
        .fromFuture(() -> existImpl(query, args))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private @NotNull CompletableFuture<Boolean> existImpl(@NotNull String query, @NotNull List<?> args) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .thenApply(queryResult -> !queryResult.getRows().isEmpty());
  }

  protected @NotNull Mono<Long> insert(@NotNull String query, @NotNull List<?> args) {
    return Mono
        .fromFuture(() -> insertImpl(query, args))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private @NotNull CompletableFuture<@NotNull Long> insertImpl(@NotNull String query, @NotNull List<?> args) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> {
          var rows = queryResult.getRows();
          return notNull(rows.get(0).getLong(0));
        });
  }

  @Deprecated
  protected @NotNull Mono<T> insert(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncCreationCallback<T> callback) {
    return Mono
        .fromFuture(() -> insertImpl(query, args, callback))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private @NotNull CompletableFuture<@NotNull T> insertImpl(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncCreationCallback<T> callback) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> {
          var rows = queryResult.getRows();
          var id = notNull(rows.get(0).getLong(0));
          return callback.handle(id);
        });
  }

  protected @NotNull Mono<T> update(@NotNull String query, @NotNull List<?> args, @NotNull T entity) {
    return Mono
        .fromFuture(() -> updateImpl(query, args, entity))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private @NotNull CompletableFuture<@NotNull T> updateImpl(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull T entity) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> {

          if (entity instanceof VersionedUniqEntity versioned) {

            if (queryResult.getRowsAffected() < 1) {
              throw new NotActualObjectDaoException("The object's version " + versioned.version() + " is outdated.");
            }

            versioned.version(versioned.version() + 1);
          }

          return entity;
        });
  }

  protected <D extends Dao<T>> @NotNull Mono<T> select(
      @NotNull String query,
      @NotNull Object arg,
      @NotNull JAsyncConverter<D, T> converter) {
    return select(query, List.of(arg), converter);
  }

  protected <D extends Dao<T>> @NotNull Mono<T> select(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncConverter<D, T> converter) {
    return Mono
        .fromFuture(() -> selectImpl(query, args, converter))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private <D extends Dao<T>> @NotNull CompletableFuture<@Nullable T> selectImpl(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncConverter<D, T> converter) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> {

          var rows = queryResult.getRows();

          if (rows.isEmpty()) {
            return null;
          } else {
            return converter.convert((D) this, rows.get(0));
          }
        });
  }

  protected @NotNull Mono<Long> count(@NotNull String query, @NotNull Object arg) {
    return count(query, List.of(arg));
  }

  protected @NotNull Mono<Long> count(@NotNull String query, @NotNull List<?> args) {
    return Mono
        .fromFuture(() -> countImpl(query, args))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private CompletableFuture<@NotNull Long> countImpl(@NotNull String query, @NotNull List<?> args) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> {

          var rows = queryResult.getRows();

          if (rows.isEmpty()) {
            return 0L;
          } else {
            return notNull(rows.get(0).getLong(0));
          }
        });
  }

  protected <D extends Dao<T>> @NotNull Mono<T> selectAsync(
      @NotNull String query,
      @NotNull Object arg,
      @NotNull JAsyncLazyConverter<D, T> converter) {
    return selectAsync(query, List.of(arg), converter);
  }

  protected <D extends Dao<T>> @NotNull Mono<T> selectAsync(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncLazyConverter<D, T> converter) {
    //noinspection DataFlowIssue
    return Mono
        .fromFuture(() -> selectAsyncImpl(query, args))
        .flatMap(data -> converter.convert((D) this, data))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private @NotNull CompletableFuture<@Nullable RowData> selectAsyncImpl(
      @NotNull String query, @NotNull List<?> args) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> {

          var rows = queryResult.getRows();

          if (rows.isEmpty()) {
            return null;
          } else {
            return rows.get(0);
          }
        });
  }

  protected @NotNull Mono<Boolean> delete(@NotNull String query, @NotNull Object arg) {
    return delete(query, List.of(arg));
  }

  protected @NotNull Mono<Boolean> delete(@NotNull String query, @NotNull List<?> args) {
    return Mono
        .fromFuture(() -> deleteImpl(query, args))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private @NotNull CompletableFuture<Boolean> deleteImpl(@NotNull String query, @NotNull List<?> args) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> queryResult.getRowsAffected() > 0);
  }
  protected <D extends Dao<T>> @NotNull Flux<T> selectAll(
      @NotNull String query,
      @NotNull JAsyncConverter<D, T> converter) {
    return Flux
        .<T>create(sink -> selectAllImpl(query, converter)
            .thenAccept(objects -> {
              objects.forEach(sink::next);
              sink.complete();
            }))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private <D extends Dao<T>> CompletableFuture<@NotNull Stream<T>> selectAllImpl(
      @NotNull String query,
      @NotNull JAsyncConverter<D, T> converter) {
    return connectionPool
        .sendQuery(query)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> queryResult
            .getRows()
            .stream()
            .map(data -> converter.convert((D) this, data)));
  }

  protected <D extends Dao<T>> @NotNull Flux<T> selectAll(
     @NotNull String query,
     @NotNull List<?> args,
     @NotNull JAsyncConverter<D, T> converter) {
    return Flux
        .<T>create(sink -> selectAllImpl(query, args, converter)
            .thenAccept(objects -> {
              objects.forEach(sink::next);
              sink.complete();
            }))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  protected <D extends Dao<T>> @NotNull Mono<List<T>> selectAllAsList(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncConverter<D, T> converter) {
    return selectAll(query, args, converter)
        .collectList();
  }

  private <D extends Dao<T>> @NotNull CompletableFuture<@NotNull Stream<T>> selectAllImpl(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncConverter<D, T> converter) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> queryResult
            .getRows()
            .stream()
            .map(data -> converter.convert((D) this, data)));
  }

  protected <D extends Dao<T>, A> @NotNull Flux<T> selectAll(
      @NotNull String query,
      @NotNull A attachment,
      @NotNull JAsyncBiConverter<D, A, T> converter) {
    return Flux
        .<T>create(sink -> selectAllImpl(query, attachment, converter)
            .thenAccept(stream -> {
              stream.forEach(sink::next);
              sink.complete();
        }))
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private <D extends Dao<T>, A> @NotNull CompletableFuture<@NotNull Stream<T>> selectAllImpl(
      @NotNull String query,
      @NotNull A attachment,
      @NotNull JAsyncBiConverter<D, A, T> converter) {
    return connectionPool
        .sendQuery(query)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> queryResult
            .getRows()
            .stream()
            .map(data -> converter.convert((D) this, attachment, data)));
  }

  protected <D extends Dao<T>> @NotNull Flux<T> selectAllAsync(
      @NotNull String query, @NotNull JAsyncLazyConverter<D, T> converter) {
    return Flux
        .<Mono<T>>create(sink -> selectAllImpl(query, converter)
            .thenAccept(stream -> {
              stream.forEach(sink::next);
              sink.complete();
            }))
        .flatMap(Function.identity())
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private <D extends Dao<T>> @NotNull CompletableFuture<@NotNull Stream<@NotNull Mono<T>>> selectAllImpl(
      @NotNull String query,
      @NotNull JAsyncLazyConverter<D, T> converter) {
    return connectionPool
        .sendQuery(query)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> queryResult
            .getRows()
            .stream()
            .map(data -> converter.convert((D) this, data)));
  }

  protected <D extends Dao<T>> @NotNull Flux<T> selectAllAsync(
      @NotNull String query,
      @NotNull Object arg,
      @NotNull JAsyncLazyConverter<D, T> converter) {
    return selectAllAsync(query, List.of(arg), converter);
  }

  protected <D extends Dao<T>> @NotNull Flux<T> selectAllAsync(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncLazyConverter<D, T> converter) {
    return Flux
        .<Mono<T>>create(sink -> selectAllImpl(query, args, converter)
            .thenAccept(stream -> {
              stream.forEach(sink::next);
              sink.complete();
            }))
        .flatMap(Function.identity())
        .onErrorMap(CompletionException.class::isInstance, Throwable::getCause);
  }

  private <D extends Dao<T>> @NotNull CompletableFuture<@NotNull Stream<@NotNull Mono<T>>> selectAllImpl(
      @NotNull String query,
      @NotNull List<?> args,
      @NotNull JAsyncLazyConverter<D, T> converter) {
    return connectionPool
        .sendPreparedStatement(query, args)
        .handle(JAsyncUtils.handleException())
        .thenApply(queryResult -> queryResult
            .getRows()
            .stream()
            .map(data -> converter.convert((D) this, data)));
  }
}
