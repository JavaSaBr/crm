package crm.base.web.exception.handler;

import crm.base.web.exception.WebException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionException;

public class DefaultWebExceptionHandler implements WebExceptionHandler, Ordered {

  public static final int ORDER = -100;

  protected final ServerResponse.Context responseContext;

  public DefaultWebExceptionHandler() {
    this.responseContext = new HandlerStrategiesResponseContext(HandlerStrategies.withDefaults());
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
    return buildError(ex)
        .switchIfEmpty(Mono.error(ex))
        .flatMap(response -> response.writeTo(exchange, responseContext))
        .flatMap(aVoid -> Mono.empty());
  }

  protected @NotNull Mono<ServerResponse> buildError(@NotNull Throwable throwable) {

    if (throwable instanceof CompletionException) {
      throwable = throwable.getCause();
    }

    if (!(throwable instanceof WebException webException)) {
      return Mono.empty();
    }

    return buildError(
        webException.getStatus(),
        webException.getErrorCode(),
        webException.getMessage());
  }

  public @NotNull Mono<ServerResponse> buildError(
      @NotNull HttpStatus status, int errorCode, @NotNull String message) {
    return buildError(status.value(), errorCode, message);
  }

  public @NotNull Mono<ServerResponse> buildError(int status, int errorCode, @NotNull String message) {
    return ServerResponse
        .status(status == 0 ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status)
        .header(WebException.HEADER_ERROR_CODE, String.valueOf(errorCode))
        .header(WebException.HEADER_ERROR_MESSAGE, message)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue("{\n\t\"errorCode\": " + errorCode + ",\n\t\"errorMessage\": \"" + message + "\"\n}");
  }

  protected record HandlerStrategiesResponseContext(@NotNull HandlerStrategies strategies) implements
      ServerResponse.Context {

    @NotNull
    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
      return strategies.messageWriters();
    }

    @NotNull
    @Override
    public List<ViewResolver> viewResolvers() {
      return strategies.viewResolvers();
    }
  }
}
