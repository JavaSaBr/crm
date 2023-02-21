package crm.security.exception;

import org.jetbrains.annotations.NotNull;

public class SecurityException extends RuntimeException {

    public SecurityException(@NotNull String message) {
        super(message);
    }

    public SecurityException(@NotNull Throwable cause) {
        super(cause);
    }
}
