package con.ss.jcrm.client.web.handler;

import static com.ss.rlib.common.util.NumberUtils.toOptionalLong;
import com.ss.jcrm.client.api.SimpleContact;
import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.resource.AuthorizedParam;
import com.ss.jcrm.security.web.resource.AuthorizedResource;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.web.exception.IdNotPresentedWebException;
import com.ss.jcrm.web.util.ResponseUtils;
import con.ss.jcrm.client.web.resource.ContactOutResource;
import con.ss.jcrm.client.web.resource.CreateContactInResource;
import con.ss.jcrm.client.web.validator.ResourceValidator;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ContactHandler {

    private final ResourceValidator resourceValidator;
    private final WebRequestSecurityService webRequestSecurityService;
    private final SimpleContactDao simpleContactDao;

    public @NotNull Mono<ServerResponse> create(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, AccessRole.ORG_ADMIN)
            .zipWhen(user -> request.bodyToMono(CreateContactInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::createContact)
            .map(ContactOutResource::new)
            .flatMap(ResponseUtils::created);
    }

    public @NotNull Mono<ServerResponse> list(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request)
            .flatMap(user -> simpleContactDao.findByOrg(user.getOrganization()))
            .map(contacts -> contacts.stream()
                .map(ContactOutResource::new)
                .toArray(ContactOutResource[]::new))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> toOptionalLong(request.pathVariable("id")))
            .map(optional -> optional.orElseThrow(IdNotPresentedWebException::new))
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(res -> simpleContactDao.findByIdAndOrg(res.getParam(), res.getOrgId()))
            .map(ContactOutResource::new)
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
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
