package con.ss.jcrm.client.web.config;

import crm.client.api.dao.SimpleClientDao;
import crm.client.jasync.config.JAsyncClientConfig;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import crm.user.api.dao.UserDao;
import crm.user.jasync.config.JAsyncUserConfig;
import crm.base.web.config.ApiEndpoint;
import crm.base.web.config.BaseWebConfig;
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
    @NotNull ApiEndpoint clientApiEndpointServer() {
        return new ApiEndpoint("/client-api");
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
        @NotNull ApiEndpoint clientApiEndpoint,
        @NotNull ClientHandler clientHandler
    ) {
        var contextPath = clientApiEndpoint.contextPath();
        return RouterFunctions.route()
            .GET("%s/status".formatted(contextPath), request -> ServerResponse.ok().build())
            .GET("%s/clients".formatted(contextPath), clientHandler::list)
            .GET("%s/clients/page".formatted(contextPath), clientHandler::findPage)
            .POST("%s/client".formatted(contextPath), clientHandler::create)
            .PUT("%s/client".formatted(contextPath), clientHandler::update)
            .GET("%s/client/{id}".formatted(contextPath), clientHandler::findById)
            .build();
    }
}
