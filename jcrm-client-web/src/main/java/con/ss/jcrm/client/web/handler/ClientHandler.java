package con.ss.jcrm.client.web.handler;

import com.ss.jcrm.client.api.SimpleContact;
import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.resource.AuthorizedResource;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import con.ss.jcrm.client.web.resource.ContactOutResource;
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
    private final WebRequestSecurityService webRequestSecurityService;
    private final SimpleContactDao simpleContactDao;

    public @NotNull Mono<ServerResponse> authenticate(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, AccessRole.ORG_ADMIN)
            .zipWith(request.bodyToMono(CreateContactInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::createContact)
            .map(ContactOutResource::new)
            .flatMap(contact -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(contact));
    }


    private @NotNull Mono<? extends @NotNull SimpleContact> createContact(
        @NotNull AuthorizedResource<CreateContactInResource> authorized
    ) {

        var user = authorized.getUser();
        var resource = authorized.getResource();

        return simpleContactDao.create(
            user.getOrganization(),
            resource.getFirstName(),
            resource.getSecondName(),
            resource.getThirdName()
        );
    }
}
