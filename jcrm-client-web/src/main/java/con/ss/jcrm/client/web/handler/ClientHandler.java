package con.ss.jcrm.client.web.handler;

import com.ss.jcrm.base.utils.HasId;
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
import com.ss.jcrm.user.api.dao.UserDao;
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
            .map(ClientOutResource::new)
            .flatMap(ResponseUtils::created);
    }

    public @NotNull Mono<ServerResponse> update(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, AccessRole.ORG_ADMIN)
            .zipWhen(user -> request.bodyToMono(ClientInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::updateContact)
            .map(ClientOutResource::new)
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> list(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request)
            .flatMap(user -> simpleClientDao.findByOrg(user.getOrganization()))
            .map(contacts -> contacts.stream()
                .map(ClientOutResource::new)
                .toArray(ClientOutResource[]::new))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
        return RequestUtils.idRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(authorized -> simpleClientDao.findByIdAndOrg(authorized.getParam(), authorized.getOrgId()))
            .map(ClientOutResource::new)
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
                ClientOutResource::new,
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

        return userDao.findByIdAndOrg(resource.getAssigner(), user.getOrganization())
            .zipWhen(assigner -> userDao.findByIdsAndOrg(resource.getCurators(), assigner.getOrganization()))
            .flatMap(args -> {

                var assigner = args.getT1();
                var curators = args.getT2();

                return simpleClientDao.create(
                    assigner,
                    curators,
                    assigner.getOrganization(),
                    resource.getFirstName(),
                    resource.getSecondName(),
                    resource.getThirdName(),
                    DateUtils.toLocalDate(resource.getBirthday()),
                    toPhoneNumbers(resource.getPhoneNumbers()),
                    toEmails(resource.getEmails()),
                    toSites(resource.getSites()),
                    toMessengers(resource.getMessengers()),
                    resource.getCompany()
                );
            });
    }

    private @NotNull Mono<? extends @NotNull SimpleClient> updateContact(
        @NotNull AuthorizedResource<ClientInResource> authorized
    ) {

        var user = authorized.getUser();
        var org = user.getOrganization();
        var resource = authorized.getResource();

        return simpleClientDao.findByIdAndOrg(resource.getId(), org)
            .switchIfEmpty(Mono.error(IdNotPresentedWebException::new))
            .zipWhen(contact -> {
                if (contact.getVersion() != resource.getVersion()) {
                    return Mono.error(new ResourceIsAlreadyChangedWebException());
                } else {
                    return userDao.findByIdAndOrg(resource.getAssigner(), org);
                }
            })
            .switchIfEmpty(Mono.error(() -> ExceptionUtils.toBadRequest(
                ClientErrors.INVALID_ASSIGNER,
                ClientErrors.INVALID_ASSIGNER_MESSAGE
            )))
            .zipWhen(tuple -> userDao.findByIdsAndOrg(resource.getCurators(), org), TupleUtils::merge)
            .flatMap(tuple -> {

                var contact = tuple.getT1();
                var assigner = tuple.getT2();
                var curators = tuple.getT3();

                contact.setAssignerId(assigner.getId());
                contact.setBirthday(DateUtils.toLocalDate(resource.getBirthday()));
                contact.setCompany(resource.getCompany());
                contact.setFirstName(resource.getFirstName());
                contact.setSecondName(resource.getSecondName());
                contact.setThirdName(resource.getThirdName());
                contact.setEmails(toEmails(resource.getEmails()));
                contact.setPhoneNumbers(toPhoneNumbers(resource.getPhoneNumbers()));
                contact.setSites(toSites(resource.getSites()));
                contact.setMessengers(toMessengers(resource.getMessengers()));
                contact.setCuratorIds(curators.stream()
                    .mapToLong(HasId::getId)
                    .toArray());

                return simpleClientDao.update(contact)
                    .onErrorMap(NotActualObjectDaoException.class, ResourceIsAlreadyChangedWebException::new)
                    .map(result -> contact);
            });
    }

    private @NotNull ClientPhoneNumber[] toPhoneNumbers(@Nullable ClientPhoneNumberResource[] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultClientPhoneNumber(
                res.getCountryCode(),
                res.getRegionCode(),
                res.getPhoneNumber(),
                PhoneNumberType.of(res.getType())
            ),
            ClientPhoneNumber.EMPTY_ARRAY
        );
    }

    private @NotNull ClientEmail[] toEmails(@Nullable ClientEmailResource[] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultClientEmail(res.getEmail(), EmailType.of(res.getType())),
            ClientEmail.EMPTY_ARRAY
        );
    }

    private @NotNull ClientSite[] toSites(@Nullable ClientSiteResource[] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultContactSite(res.getUrl(), SiteType.of(res.getType())),
            ClientSite.EMPTY_ARRAY
        );
    }

    private @NotNull ClientMessenger[] toMessengers(@Nullable ClientMessengerResource[] resources) {
        return ArrayUtils.map(
            resources,
            res -> new DefaultClientMessenger(res.getLogin(), MessengerType.of(res.getType())),
            ClientMessenger.EMPTY_ARRAY
        );
    }
}
