package crm.base.web.util;

import crm.base.web.exception.BadRequestWebException;
import crm.base.web.exception.ForbiddenWebException;
import crm.base.web.exception.UnauthorizedWebException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletionException;
import java.util.function.Predicate;

public class ExceptionUtils {

  public static <T> @NotNull T badRequest(
      @NotNull Throwable throwable, @NotNull Predicate<Throwable> expected, int errorCode) {
    if (expected.test(throwable)) {
      throw new BadRequestWebException(errorCode);
    } else {
      throw new CompletionException(throwable);
    }
  }

  public static <T> @NotNull T badRequest(
      @NotNull Throwable throwable, @NotNull Predicate<Throwable> expected, int errorCode, @NotNull String message) {
    if (expected.test(throwable)) {
      throw new BadRequestWebException(message, errorCode);
    } else {
      throw new CompletionException(throwable);
    }
  }

  public static @NotNull RuntimeException toBadRequest(
      @NotNull Throwable throwable, @NotNull Predicate<Throwable> expected, int errorCode, @NotNull String message) {
    if (expected.test(throwable)) {
      return new BadRequestWebException(message, errorCode);
    } else {
      return new CompletionException(throwable);
    }
  }

  public static @NotNull BadRequestWebException toBadRequest(int errorCode, @NotNull String message) {
    return new BadRequestWebException(message, errorCode);
  }

  public static <T> @NotNull T unauthorized(int errorCode, @NotNull String message) {
    throw new UnauthorizedWebException(message, errorCode);
  }

  public static @NotNull UnauthorizedWebException toUnauthorized(
      int errorCode, @NotNull String message) {
    return new UnauthorizedWebException(message, errorCode);
  }

  public static <T> @NotNull T unauthorized(
      @NotNull Throwable throwable,
      @NotNull Predicate<Throwable> expected,
      int errorCode,
      @NotNull String message) {
    if (expected.test(throwable)) {
      throw new UnauthorizedWebException(message, errorCode);
    } else {
      throw new CompletionException(throwable);
    }
  }

  public static <T> @NotNull T forbidden(int errorCode, @NotNull String message) {
    throw new ForbiddenWebException(message, errorCode);
  }

  public static @NotNull ForbiddenWebException toForbidden(int errorCode, @NotNull String message) {
    return new ForbiddenWebException(message, errorCode);
  }
}
