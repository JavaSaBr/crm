package crm.base.web.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebException extends RuntimeException {

  public static final String HEADER_ERROR_CODE = "crm-error-code";
  public static final String HEADER_ERROR_MESSAGE = "crm-error-message";

  int status;
  int errorCode;

  public WebException(int status, int errorCode) {
    super("No description");
    this.status = status;
    this.errorCode = errorCode;
  }

  public WebException(int status, @NotNull String message, int errorCode) {
    super(message);
    this.status = status;
    this.errorCode = errorCode;
  }

  public WebException(int status, @NotNull String message) {
    super(message);
    this.status = status;
    this.errorCode = 0;
  }

  public WebException(int status, @NotNull Throwable cause, @NotNull String message, int errorCode) {
    super(message, cause);
    this.status = status;
    this.errorCode = errorCode;
  }
}
