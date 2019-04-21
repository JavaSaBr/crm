package com.ss.jcrm.ui.server.config;

import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import com.ss.rlib.common.util.FileUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("com.ss.jcrm.ui.server.controller")
public class UiServerConfig {

    @AllArgsConstructor
    public static class AngularWebFilter implements WebFilter {

        private final String[] apiEndpoints;

        @Override
        public @NotNull Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {

            var request = exchange.getRequest();
            var path = request.getURI().getPath();

            if (apiEndpoints.length > 0) {
                for (var endpoint : apiEndpoints) {
                    if (path.startsWith(endpoint)) {
                        return chain.filter(exchange);
                    }
                }
            }

            var extension = FileUtils.getExtension(path);

            if (!extension.isEmpty()) {
                return chain.filter(exchange);
            }

            var newRequest = request.mutate()
                .path("/index.html")
                .build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        }
    }

    private final String[] apiEndpoints;

    @Autowired(required = false)
    public UiServerConfig(@NotNull List<ApiEndpointServer> servers) {
        this.apiEndpoints = servers.stream()
            .map(ApiEndpointServer::getContextPath)
            .toArray(String[]::new);
    }

    @Bean
    @NotNull WebFilter angularWebFilter() {
        return new AngularWebFilter(apiEndpoints);
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
