package con.ss.jcrm.client.web.handler;

import com.ss.jcrm.web.exception.UnauthorizedWebException;
import con.ss.jcrm.client.web.resource.CreateContactInResource;
import con.ss.jcrm.client.web.validator.ResourceValidator;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ClientHandler {

    private final ResourceValidator resourceValidator;

    public @NotNull Mono<ServerResponse> authenticate(@NotNull ServerRequest request) {
        return request.bodyToMono(CreateContactInResource.class)
            .doOnNext(resourceValidator::validate)
            .zipWhen(this::loadUserByLogin, this::authenticate)
            .switchIfEmpty(Mono.error(() -> new UnauthorizedWebException(
                INVALID_CREDENTIALS_MESSAGE,
                INVALID_CREDENTIALS
            )))
            .flatMap(resource -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(resource));
    }
}
