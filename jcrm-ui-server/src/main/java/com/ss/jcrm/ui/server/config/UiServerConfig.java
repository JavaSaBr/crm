package com.ss.jcrm.ui.server.config;

import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import com.ss.rlib.common.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class UiServerConfig {

    public static class CustomWebFilter implements WebFilter {

        @Override
        public @NotNull Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {

            var request = exchange.getRequest();
            var path = request.getURI().getPath();
            var extension = FileUtils.getExtension(path);

            if (extension.isEmpty()) {

                var newRequest = request.mutate()
                    .path("/index.html")
                    .build();

                return chain.filter(exchange.mutate().request(newRequest).build());
            }

            return chain.filter(exchange);
        }
    }

    @Bean
    @NotNull WebFilter customWebFilter() {
        return new CustomWebFilter();
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> staticResourceRouter() {
        return resources("/**", new ClassPathResource("com/ss/jcrm/ui/server/"));
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> htmlRouter(
        @NotNull @Value("classpath:com/ss/jcrm/ui/server/index.html") Resource html
    ) {

        HandlerFunction<ServerResponse> function = request -> ok()
            .contentType(MediaType.TEXT_HTML)
            .syncBody(html);

        return route().GET("/", function)
            .build();
    }
}
