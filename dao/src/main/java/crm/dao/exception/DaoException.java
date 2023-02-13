package crm.dao.exception;

public class DaoException extends RuntimeException {

    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
