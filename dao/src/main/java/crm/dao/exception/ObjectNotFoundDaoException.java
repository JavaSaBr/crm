package crm.dao.exception;

import org.jetbrains.annotations.NotNull;

public class ObjectNotFoundDaoException extends DaoException {

    public ObjectNotFoundDaoException(@NotNull String message) {
        super(message);
    }
}
