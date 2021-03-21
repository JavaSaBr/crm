package con.ss.jcrm.client.web.config;

import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.client.jasync.config.JAsyncClientConfig;
import com.ss.jcrm.security.web.WebSecurityConfig;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.jasync.config.JAsyncUserConfig;
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

    @Autowired
    private final Environment env;

    @Autowired
    private List<? extends Flyway> flyways;

    @Bean
    @NotNull ApiEndpointServer clientApiEndpointServer() {
        return new ApiEndpointServer("/client");
    }

    @Bean
    @NotNull ResourceValidator clientResourceValidator() {
        return new ResourceValidator(env);
    }

    @Bean
    @NotNull ContactHandler contactHandler(
        @NotNull ResourceValidator resourceValidator,
        @NotNull WebRequestSecurityService webRequestSecurityService,
        @NotNull SimpleContactDao simpleContactDao,
        @NotNull UserDao userDao
    ) {
        return new ContactHandler(resourceValidator, webRequestSecurityService, simpleContactDao, userDao);
    }

    @Bean
    @NotNull RouterFunction<ServerResponse> clientStatusRouterFunction(
        @NotNull ApiEndpointServer clientApiEndpointServer,
        @NotNull ContactHandler contactHandler
    ) {

        var contextPath = clientApiEndpointServer.contextPath();

        return RouterFunctions.route()
            .GET(contextPath + "/status", request -> ServerResponse.ok()
                .build())
            .GET(contextPath + "/contacts", contactHandler::list)
            .GET(contextPath + "/contacts/page", contactHandler::findPage)
            .POST(contextPath + "/contact", contactHandler::create)
            .PUT(contextPath + "/contact", contactHandler::update)
            .GET(contextPath + "/contact/{id}", contactHandler::findById)
            .build();
    }
}
