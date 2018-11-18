package com.ss.jcrm.dao.exception;

import org.jetbrains.annotations.NotNull;

public class NotActualObjectDaoException extends DaoException {

    public NotActualObjectDaoException(@NotNull String message) {
        super(message);
    }
}
