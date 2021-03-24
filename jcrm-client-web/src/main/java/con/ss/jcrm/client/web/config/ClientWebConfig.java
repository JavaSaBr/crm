package con.ss.jcrm.client.web.config;

import com.ss.jcrm.client.api.dao.SimpleClientDao;
import com.ss.jcrm.client.jasync.config.JAsyncClientConfig;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.jasync.config.JAsyncUserConfig;
import com.ss.jcrm.web.config.ApiEndpointServer;
import com.ss.jcrm.web.config.BaseWebConfig;
import con.ss.jcrm.client.web.handler.ClientHandler;
import con.ss.jcrm.client.web.validator.ResourceValidator;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

@Import({
    BaseWebConfig.class,
    JAsyncClientConfig.class,
    JAsyncUserConfig.class,
    WebSecurityConfig.class
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

    private final List<? extends Flyway> flyways;

    @Bean
    @NotNull ApiEndpointServer clientApiEndpointServer() {
        return new ApiEndpointServer("/client");
    }

    @Bean
    @NotNull ResourceValidator clientResourceValidator(@NotNull Environment env) {
        return new ResourceValidator(env);
    }

    @Bean
    @NotNull ClientHandler contactHandler(
        @NotNull ResourceValidator resourceValidator,
        @NotNull WebRequestSecurityService webRequestSecurityService,
        @NotNull SimpleClientDao simpleClientDao,
        @NotNull UserDao userDao
    ) {
        return new ClientHandler(resourceValidator, webRequestSecurityService, simpleClientDao, userDao);
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> clientStatusRouterFunction(
        @NotNull ApiEndpointServer clientApiEndpointServer,
        @NotNull ClientHandler clientHandler
    ) {

        var contextPath = clientApiEndpointServer.contextPath();
        return RouterFunctions.route()
            .GET("${contextPath}/status", request -> ServerResponse.ok().build())
            .GET("${contextPath}/contacts", clientHandler::list)
            .GET("${contextPath}/contacts/page", clientHandler::findPage)
            .POST("${contextPath}/contact", clientHandler::create)
            .PUT("${contextPath}/contact", clientHandler::update)
            .GET("${contextPath}/contact/{id}", clientHandler::findById)
            .build();
    }
}
