package crm.ui.server.config;

import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import crm.base.web.config.ApiEndpoint;
import crm.base.web.config.BaseWebConfig;
import com.ss.rlib.common.util.FileUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@Import(BaseWebConfig.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UiServerConfig {

  @RequiredArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public class FrontEndWebFilter implements WebFilter {

    @NotNull List<String> endpoints;

    @Override
    public @NotNull Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {

      var request = exchange.getRequest();
      var path = request
          .getURI()
          .getPath();

      if (FileUtils.hasExtension(path)) {
        return chain.filter(exchange);
      } else {
        for (var endpoint : endpoints) {
          if (path.startsWith(endpoint)) {
            return chain.filter(exchange);
          }
        }
      }

      var newRequest = request
          .mutate()
          .path("/index.html")
          .build();

      return chain.filter(exchange
          .mutate()
          .request(newRequest)
          .build());
    }
  }

  @Bean
  @NotNull WebFilter frontEndWebFilter(@NotNull List<ApiEndpoint> endpoints) {
    return new FrontEndWebFilter(endpoints
        .stream()
        .map(ApiEndpoint::contextPath)
        .toList());
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> staticResourceRouter() {
    return resources("/**", new ClassPathResource("com/ss/jcrm/ui/server/"));
  }

  @Bean
  @NotNull RouterFunction<ServerResponse> htmlRouter(
      @Value("classpath:com/ss/jcrm/ui/server/index.html") @NotNull Resource html) {

    HandlerFunction<ServerResponse> function = request -> ok()
        .contentType(MediaType.TEXT_HTML)
        .syncBody(html);

    return route()
        .GET("/", function)
        .build();
  }
}
