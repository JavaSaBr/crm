package com.ss.jcrm.jdbc.util;

import static java.util.stream.Collectors.toUnmodifiableSet;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.ss.jcrm.base.utils.HasId;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.rlib.common.function.ObjectLongFunction;
import com.ss.rlib.common.util.StringUtils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.LongFunction;
import java.util.stream.LongStream;

@UtilityClass
public class JdbcUtils {

    public @NotNull DaoException convert(@NotNull SQLException exception) {

        if (!(exception instanceof PSQLException)) {
            return new DaoException(exception);
        }

        var psqlEx = (PSQLException) exception;

        switch (psqlEx.getSQLState()) {
            case "23505":
                return new DuplicateObjectDaoException(psqlEx.getServerErrorMessage().getMessage());
        }

        return new DaoException(exception);
    }

    public static <T> @NotNull Set<T> fromJsonArray(
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

    public static <T, A> @NotNull Set<T> fromJsonArray(
        @Nullable String json,
        @NotNull A argument,
        @NotNull ObjectLongFunction<A, T> function
    ) {

        if (StringUtils.isEmpty(json)) {
            return Set.of();
        }

        var deserialize = JsonIterator.deserialize(json, long[].class);

        if (deserialize == null || deserialize.length < 1) {
            return Set.of();
        }

        return LongStream.of(deserialize)
            .mapToObj(value -> function.apply(argument, value))
            .collect(toUnmodifiableSet());
    }

    public static <T extends HasId> @Nullable PGobject toJsonArray(@NotNull Set<T> entities)
        throws SQLException {

        if (entities.isEmpty()) {
            return null;
        }

        var result = new PGobject();
        result.setType("json");
        result.setValue(JsonStream.serialize(entities.stream()
            .mapToLong(HasId::getId)
            .toArray()));

        return result;
    }
}
