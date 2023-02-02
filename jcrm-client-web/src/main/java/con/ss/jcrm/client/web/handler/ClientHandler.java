package con.ss.jcrm.client.web.handler;

import com.ss.jcrm.base.utils.WithId;
import com.ss.jcrm.client.api.*;
import com.ss.jcrm.client.api.dao.SimpleClientDao;
import com.ss.jcrm.client.api.impl.DefaultClientEmail;
import com.ss.jcrm.client.api.impl.DefaultClientMessenger;
import com.ss.jcrm.client.api.impl.DefaultClientPhoneNumber;
import com.ss.jcrm.client.api.impl.DefaultContactSite;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.resource.AuthorizedParam;
import com.ss.jcrm.security.web.resource.AuthorizedResource;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import crm.user.api.dao.UserDao;
import com.ss.jcrm.web.exception.ExceptionUtils;
import com.ss.jcrm.web.exception.IdNotPresentedWebException;
import com.ss.jcrm.web.exception.ResourceIsAlreadyChangedWebException;
import com.ss.jcrm.web.resources.DataPageResponse;
import com.ss.jcrm.web.util.RequestUtils;
import com.ss.jcrm.web.util.ResponseUtils;
import com.ss.jcrm.web.util.TupleUtils;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.DateUtils;
import con.ss.jcrm.client.web.exception.ClientErrors;
import con.ss.jcrm.client.web.resource.*;
import con.ss.jcrm.client.web.validator.ResourceValidator;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Value
public class ClientHandler {

    @NotNull ResourceValidator resourceValidator;
    @NotNull WebRequestSecurityService webRequestSecurityService;
    @NotNull SimpleClientDao simpleClientDao;
    @NotNull UserDao userDao;

    public @NotNull Mono<ServerResponse> create(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, AccessRole.ORG_ADMIN)
            .zipWhen(user -> request.bodyToMono(ClientInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::createContact)
            .map(ClientOutResource::from)
            .flatMap(ResponseUtils::created);
    }

    public @NotNull Mono<ServerResponse> update(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, AccessRole.ORG_ADMIN)
            .zipWhen(user -> request.bodyToMono(ClientInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::updateContact)
            .map(ClientOutResource::from)
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> list(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request)
            .flatMap(user -> simpleClientDao.findByOrg(user.organization()))
            .map(contacts -> contacts.stream()
                .map(ClientOutResource::from)
                .toArray(ClientOutResource[]::new))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
        return RequestUtils.idRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(authorized -> simpleClientDao.findByIdAndOrg(authorized.getParam(), authorized.getOrgId()))
            .map(ClientOutResource::from)
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findPage(@NotNull ServerRequest request) {
        return RequestUtils.pageRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(authorized -> {
                var pageRequest = authorized.getParam();
                return simpleClientDao.findPageByOrg(
                    pageRequest.offset(),
                    pageRequest.pageSize(),
                    authorized.getOrgId()
                );
            })
            .map(entityPage -> DataPageResponse.from(
                entityPage.totalSize(),
                entityPage.entities(),
                ClientOutResource::from,
                ClientOutResource[]::new
            ))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    private @NotNull Mono<? extends @NotNull SimpleClient> createContact(
        @NotNull AuthorizedResource<ClientInResource> authorized
    ) {

        var user = authorized.getUser();
        var resource = authorized.getResource();

        return userDao.findByIdAndOrg(resource.assigner(), user.organization())
            .zipWhen(assigner -> userDao.findByIdsAndOrg(resource.curators(), assigner.organization()))
            .flatMap(args -> {

                var assigner = args.getT1();
                var curators = args.getT2();

                return simpleClientDao.create(
                    assigner,
                    curators,
                    assigner.organization(),
                    resource.firstName(),
                    resource.secondName(),
                    resource.thirdName(),
                    DateUtils.toLocalDate(resource.birthday()),
                    toPhoneNumbers(resource.phoneNumbers()),
                    toEmails(resource.emails()),
                    toSites(resource.sites()),
                    toMessengers(resource.messengers()),
                    resource.company()
                );
            });
    }

    private @NotNull Mono<? extends @NotNull SimpleClient> updateContact(
        @NotNull AuthorizedResource<ClientInResource> authorized
    ) {

        var user = authorized.getUser();
        var org = user.organization();
        var resource = authorized.getResource();

        return simpleClientDao.findByIdAndOrg(resource.id(), org)
            .switchIfEmpty(Mono.error(IdNotPresentedWebException::new))
            .zipWhen(contact -> {
                if (contact.version() != resource.version()) {
                    return Mono.error(new ResourceIsAlreadyChangedWebException());
                } else {
                    return userDao.findByIdAndOrg(resource.assigner(), org);
                }
            })
            .switchIfEmpty(Mono.error(() -> ExceptionUtils.toBadRequest(
                ClientErrors.INVALID_ASSIGNER,
                ClientErrors.INVALID_ASSIGNER_MESSAGE
            )))
            .zipWhen(tuple -> userDao.findByIdsAndOrg(resource.curators(), org), TupleUtils::merge)
            .flatMap(tuple -> {

                var contact = tuple.getT1();
                var assigner = tuple.getT2();
                var curators = tuple.getT3();

                contact.setAssignerId(assigner.getId());
                contact.setBirthday(DateUtils.toLocalDate(resource.birthday()));
                contact.setCompany(resource.company());
                contact.setFirstName(resource.firstName());
                contact.setSecondName(resource.secondName());
                contact.setThirdName(resource.thirdName());
                contact.setEmails(toEmails(resource.emails()));
                contact.setPhoneNumbers(toPhoneNumbers(resource.phoneNumbers()));
                contact.setSites(toSites(resource.sites()));
                contact.setMessengers(toMessengers(resource.messengers()));
                contact.setCuratorIds(curators.stream()
                    .mapToLong(WithId::id)
                    .toArray());

                return simpleClientDao.update(contact)
                    .onErrorMap(NotActualObjectDaoException.class, ResourceIsAlreadyChangedWebException::new)
                    .map(result -> contact);
            });
    }

    private @NotNull ClientPhoneNumber[] toPhoneNumbers(ClientPhoneNumberResource @Nullable [] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultClientPhoneNumber(
                res.countryCode(),
                res.regionCode(),
                res.phoneNumber(),
                PhoneNumberType.of(res.type())
            ),
            ClientPhoneNumber.EMPTY_ARRAY
        );
    }

    private ClientEmail @NotNull [] toEmails(ClientEmailResource @Nullable [] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultClientEmail(res.email(), EmailType.of(res.type())),
            ClientEmail.EMPTY_ARRAY
        );
    }

    private ClientSite @NotNull [] toSites(ClientSiteResource @Nullable [] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultContactSite(res.url(), SiteType.of(res.type())),
            ClientSite.EMPTY_ARRAY
        );
    }

    private ClientMessenger @NotNull [] toMessengers(ClientMessengerResource @Nullable [] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultClientMessenger(res.login(), MessengerType.of(res.type())),
            ClientMessenger.EMPTY_ARRAY
        );
    }
}
