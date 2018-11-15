package com.ss.jcrm.jdbc.util;

import com.ss.jcrm.jdbc.exception.DuplicateJdbcException;
import com.ss.jcrm.jdbc.exception.JdbcException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

@UtilityClass
public class JdbcUtils {

    public @NotNull JdbcException convert(@NotNull SQLException exception) {

        if (!(exception instanceof PSQLException)) {
            return new JdbcException(exception);
        }

        var psqlEx = (PSQLException) exception;

        switch (psqlEx.getSQLState()) {
            case "23505":
                return new DuplicateJdbcException(psqlEx.getServerErrorMessage().getMessage());
        }

        return new JdbcException(exception);
    }
}
