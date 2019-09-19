package com.ss.jcrm.jasync.util;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableSet;
import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.exceptions.GenericDatabaseException;
import com.github.jasync.sql.db.util.ExecutorServiceUtils;
import com.github.jasync.sql.db.util.NettyUtils;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.ss.jcrm.base.utils.HasId;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.rlib.common.function.ObjectLongFunction;
import com.ss.rlib.common.util.ObjectUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.Array;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class JAsyncUtils {

    private static final long[] EMPTY_IDS = new long[0];

    public static <T> @NotNull BiFunction<T, Throwable, T> handleException() {

        return (result, throwable) -> {

            var exc = throwable;

            if (exc instanceof CompletionException) {
                exc = exc.getCause();
            }

            if (exc instanceof GenericDatabaseException) {
                throw convert((GenericDatabaseException) exc);
            } else if (exc instanceof RuntimeException) {
                throw (RuntimeException) exc;
            } else if (exc != null) {
                throw new RuntimeException(exc);
            }

            return result;
        };
    }

    public static @NotNull DaoException convert(@NotNull GenericDatabaseException exception) {

        var errorMessage = exception.getErrorMessage();
        var fields = errorMessage.getFields();


        switch (fields.get('C')) {
            case "23505": {
                return new DuplicateObjectDaoException(String.valueOf(errorMessage.getMessage()));
            }
        }

        return new DaoException(exception);
    }

    public static @NotNull ConnectionPoolConfiguration buildPoolConfig(
        @NotNull Configuration configuration,
        @NotNull PoolConfiguration dbPoolConfiguration,
        @Nullable EventLoopGroup eventLoopGroup,
        @Nullable Executor dbExecutor
    ) {

        return new ConnectionPoolConfiguration(
            configuration.getHost(),
            configuration.getPort(),
            configuration.getDatabase(),
            configuration.getUsername(),
            configuration.getPassword(),
            dbPoolConfiguration.getMaxObjects(),
            dbPoolConfiguration.getMaxIdle(),
            dbPoolConfiguration.getMaxQueueSize(),
            dbPoolConfiguration.getValidationInterval(),
            dbPoolConfiguration.getCreateTimeout(),
            dbPoolConfiguration.getTestTimeout(),
            dbPoolConfiguration.getQueryTimeout(),
            ObjectUtils.ifNull(eventLoopGroup, NettyUtils.INSTANCE.getDefaultEventLoopGroup()),
            ObjectUtils.ifNull(dbExecutor, ExecutorServiceUtils.INSTANCE.getCommonPool()),
            dbPoolConfiguration.getCoroutineDispatcher(),
            configuration.getSsl(),
            configuration.getCharset(),
            configuration.getMaximumMessageSize(),
            PooledByteBufAllocator.DEFAULT,
            "JCRM",
            emptyList(),
            dbPoolConfiguration.getMaxObjectTtl()
        );
    }

    public static @Nullable LocalDate toDate(@Nullable java.time.LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            return new LocalDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        }
    }

    public static @NotNull LocalDateTime toDateTime(@NotNull Instant instant) {
        return new LocalDateTime(instant.toEpochMilli());
    }

    public static @NotNull Instant toJavaInstant(@NotNull LocalDateTime dateTime) {
        return dateTime.toDate().toInstant();
    }

    public static @Nullable java.time.LocalDate toJavaDate(@Nullable LocalDate dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            return java.time.LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        }
    }

    public static <T> @Nullable T[] arrayFromJson(@Nullable String json, @NotNull Class<T[]> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            return JsonIterator.deserialize(json, type);
        }
    }

    public static <I, T extends I> @Nullable I[] arrayFromJson(
        @Nullable String json,
        @NotNull Class<T[]> type,
        @NotNull I[] def
    ) {
        if (StringUtils.isEmpty(json)) {
            return def;
        } else {
            return ObjectUtils.ifNull(JsonIterator.deserialize(json, type), def);
        }
    }

    public static <T> @NotNull Set<T> fromJsonIds(
        @Nullable String json,
        @NotNull LongFunction<T> function
    ) {

        if (StringUtils.isEmpty(json)) {
            return Set.of();
        }

        var deserialize = JsonIterator.deserialize(json, long[].class);

        if (deserialize == null || deserialize.length < 1) {
            return Set.of();
        }

        return LongStream.of(deserialize)
            .mapToObj(function)
            .collect(toUnmodifiableSet());
    }

    public static <T> @NotNull Mono<@NotNull Set<T>> fromJsonIdsAsync(
        @Nullable String json,
        @NotNull LongFunction<Mono<T>> function
    ) {

        if (StringUtils.isEmpty(json) || "[]".equals(json)) {
            return Mono.just(Set.of());
        }

        var deserialize = JsonIterator.deserialize(json, long[].class);

        if (deserialize == null || deserialize.length < 1) {
            return Mono.just(Set.of());
        }

        return Flux.concat(LongStream.of(deserialize)
            .mapToObj(function)
            .collect(toList()))
            .collect(() -> new HashSet<T>(), Set::add)
            .map(Collections::unmodifiableSet);
    }

    public static <T, A> @NotNull Mono<@NotNull Set<T>> fromJsonIdsAsync(
        @Nullable String json,
        @NotNull A argument,
        @NotNull ObjectLongFunction<A, Mono<T>> function
    ) {

        if (StringUtils.isEmpty(json)) {
            return Mono.just(Set.of());
        }

        var deserialize = JsonIterator.deserialize(json, long[].class);

        if (deserialize == null || deserialize.length < 1) {
            return Mono.just(Set.of());
        }

        return Flux.concat(LongStream.of(deserialize)
            .mapToObj(value -> function.apply(argument, value))
            .collect(toList()))
            .collect(() -> new HashSet<T>(), Set::add)
            .map(Collections::unmodifiableSet);
    }

    public static <T extends HasId> long @NotNull [] toIds(@Nullable Array<T> entities) {

        if (entities == null || entities.isEmpty()) {
            return EMPTY_IDS;
        }

        return entities.stream()
            .mapToLong(HasId::getId)
            .toArray();
    }

    public static <T extends HasId> long @NotNull [] toIds(@Nullable T[] entities) {

        if (entities == null || entities.length < 1) {
            return EMPTY_IDS;
        }

        return Stream.of(entities)
            .mapToLong(HasId::getId)
            .toArray();
    }

    public static long @NotNull [] jsonToIds(@Nullable String json) {

        if (StringUtils.isEmpty(json)) {
            return EMPTY_IDS;
        }

        var deserialize = JsonIterator.deserialize(json, long[].class);

        if (deserialize == null || deserialize.length < 1) {
            return EMPTY_IDS;
        } else {
            return deserialize;
        }
    }

    public static <T extends HasId> @Nullable String idsToJson(@Nullable T[] entities) {

        if (entities == null || entities.length < 1) {
            return null;
        }

        return JsonStream.serialize(Stream.of(entities)
            .mapToLong(HasId::getId)
            .toArray());
    }

    public static <T extends HasId> @Nullable String idsToJson(@Nullable Array<T> entities) {

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        return JsonStream.serialize(entities.stream()
            .mapToLong(HasId::getId)
            .toArray());
    }

    public static <T extends HasId> @Nullable String idsToJson(@NotNull Set<T> entities) {

        if (entities.isEmpty()) {
            return null;
        }

        return JsonStream.serialize(entities.stream()
            .mapToLong(HasId::getId)
            .toArray());
    }

    public static @Nullable String toJson(@Nullable long[] ids) {
        if (ids == null || ids.length < 1) {
            return null;
        } else {
            return JsonStream.serialize(ids);
        }
    }

    public static <T> @Nullable String toJson(@Nullable T[] entities) {
        if (entities == null || entities.length < 1) {
            return null;
        } else {
            return JsonStream.serialize(entities);
        }
    }

    public static @NotNull ConnectionPoolConfiguration buildPoolConfig(
        @NotNull Configuration configuration,
        @NotNull PoolConfiguration dbPoolConfiguration
    ) {
        return buildPoolConfig(configuration, dbPoolConfiguration, null, null);
    }

    public static @NotNull Configuration buildConfiguration(
        @NotNull String username,
        @NotNull String password,
        @NotNull String host,
        int port,
        @NotNull String database
    ) {
        return new Configuration(
            username,
            host,
            port,
            password,
            database
        );
    }
}
