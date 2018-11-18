package com.ss.jcrm.dao.exception;

import org.jetbrains.annotations.NotNull;

public class DuplicateObjectDaoException extends DaoException {

    public DuplicateObjectDaoException(@NotNull String message) {
        super(message);
    }
}
