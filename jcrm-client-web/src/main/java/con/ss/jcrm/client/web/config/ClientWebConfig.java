package con.ss.jcrm.client.web.config;

import com.ss.jcrm.client.jasync.config.JAsyncClientConfig;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

@Import({
    JAsyncClientConfig.class,
    WebSecurityConfig.class,
    BaseWebConfig.class
})
@Configuration
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/client/web/client-web.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/client/web/client-web-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@RequiredArgsConstructor
public class ClientWebConfig {

    @Autowired
    private final Environment env;

    @Autowired
    private List<? extends Flyway> flyways;

    @Bean
    @NotNull ApiEndpointServer clientApiEndpointServer() {
        return new ApiEndpointServer("/client");
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> clientStatusRouterFunction(
        @NotNull ApiEndpointServer clientApiEndpointServer
    ) {
        var contextPath = clientApiEndpointServer.getContextPath();
        return RouterFunctions.route()
            .GET(contextPath + "/status", request -> ServerResponse.ok()
                .build())
            .build();
    }
}
