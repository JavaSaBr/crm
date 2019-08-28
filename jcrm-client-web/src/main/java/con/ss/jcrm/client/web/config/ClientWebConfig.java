package con.ss.jcrm.client.web.config;

import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.client.jasync.config.JAsyncClientConfig;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import con.ss.jcrm.client.web.handler.ContactHandler;
import con.ss.jcrm.client.web.validator.ResourceValidator;
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
    @NotNull ResourceValidator resourceValidator() {
        return new ResourceValidator();
    }

    @Bean
    @NotNull ContactHandler contactHandler(
        @NotNull ResourceValidator resourceValidator,
        @NotNull WebRequestSecurityService webRequestSecurityService,
        @NotNull SimpleContactDao simpleContactDao
    ) {
        return new ContactHandler(resourceValidator, webRequestSecurityService, simpleContactDao);
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> clientStatusRouterFunction(
        @NotNull ApiEndpointServer clientApiEndpointServer,
        @NotNull ContactHandler contactHandler
    ) {
        var contextPath = clientApiEndpointServer.getContextPath();
        return RouterFunctions.route()
            .GET(contextPath + "/status", request -> ServerResponse.ok()
                .build())
            .GET(contextPath + "/contacts", contactHandler::list)
            .POST(contextPath + "/contact/create", contactHandler::create)
            .build();
    }
}
