package crm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class PrematureTokenException extends TokenException {
  public PrematureTokenException(@NotNull String message) {
    super(message);
  }
}
