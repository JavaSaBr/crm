package com.ss.jcrm.jdbc.util;

import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

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
}
