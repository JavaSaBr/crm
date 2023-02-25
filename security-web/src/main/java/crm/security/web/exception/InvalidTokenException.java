package crm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidTokenException extends TokenException {

  public InvalidTokenException(@NotNull Throwable cause) {
    super(cause);
  }

  public InvalidTokenException(@NotNull String message) {
    super(message);
  }
}
