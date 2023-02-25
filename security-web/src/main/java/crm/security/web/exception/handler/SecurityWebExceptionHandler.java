package crm.security.web.exception.handler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import crm.security.web.exception.ExpiredTokenException;
import crm.security.web.exception.MaxRefreshedTokenException;
import crm.security.web.exception.TokenException;
import crm.base.web.exception.handler.DefaultWebExceptionHandler;
import crm.security.web.exception.SecurityErrors;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionException;

@Order(SecurityWebExceptionHandler.ORDER)
public class SecurityWebExceptionHandler extends DefaultWebExceptionHandler implements WebExceptionHandler {

  public static final int ORDER = DefaultWebExceptionHandler.ORDER - 1;

  @Override
  public int getOrder() {
    return ORDER;
  }

  protected @NotNull Mono<ServerResponse> buildError(@NotNull Throwable throwable) {

    if (throwable instanceof CompletionException) {
      throwable = throwable.getCause();
    }

    if (!(throwable instanceof TokenException)) {
      return Mono.empty();
    }

    if (throwable instanceof ExpiredTokenException) {
      return buildError(UNAUTHORIZED, SecurityErrors.EXPIRED_TOKEN, SecurityErrors.EXPIRED_TOKEN_MESSAGE);
    } else if (throwable instanceof MaxRefreshedTokenException) {
      return buildError(UNAUTHORIZED, SecurityErrors.MAX_REFRESHED_TOKEN, SecurityErrors.MAX_REFRESHED_TOKEN_MESSAGE);
    }

    return buildError(UNAUTHORIZED, SecurityErrors.INVALID_TOKEN, SecurityErrors.INVALID_TOKEN_MESSAGE);
  }
}
