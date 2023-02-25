package crm.security.web.exception;

import org.jetbrains.annotations.NotNull;

public class MaxRefreshedTokenException extends TokenException {
  public MaxRefreshedTokenException(@NotNull String message) {
    super(message);
  }
}
