package con.ss.jcrm.client.web.handler;

import crm.base.util.WithId;
import crm.client.api.dao.SimpleClientDao;
import crm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.resource.AuthorizedParam;
import com.ss.jcrm.security.web.resource.AuthorizedResource;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import crm.client.api.SimpleClient;
import crm.contact.api.resource.EmailResource;
import crm.contact.api.resource.MessengerResource;
import crm.contact.api.resource.PhoneNumberResource;
import crm.contact.api.resource.SiteResource;
import crm.user.api.dao.UserDao;
import crm.base.web.util.ExceptionUtils;
import crm.base.web.exception.IdNotPresentedWebException;
import crm.base.web.exception.ResourceIsAlreadyChangedWebException;
import crm.base.web.resources.DataPageResponse;
import crm.base.web.util.RequestUtils;
import crm.base.web.util.ResponseUtils;
import crm.base.web.util.TupleUtils;
import com.ss.rlib.common.util.DateUtils;
import con.ss.jcrm.client.web.exception.ClientErrors;
import con.ss.jcrm.client.web.resource.*;
import con.ss.jcrm.client.web.validator.ResourceValidator;
import java.util.Set;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
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
    return webRequestSecurityService
        .isAuthorized(request, AccessRole.ORG_ADMIN)
        .zipWhen(user -> request.bodyToMono(ClientInResource.class), AuthorizedResource::new)
        .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
        .flatMap(this::createContact)
        .map(ClientOutResource::from)
        .flatMap(ResponseUtils::created);
  }

  public @NotNull Mono<ServerResponse> update(@NotNull ServerRequest request) {
    return webRequestSecurityService
        .isAuthorized(request, AccessRole.ORG_ADMIN)
        .zipWhen(user -> request.bodyToMono(ClientInResource.class), AuthorizedResource::new)
        .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
        .flatMap(this::updateContact)
        .map(ClientOutResource::from)
        .flatMap(ResponseUtils::ok);
  }

  public @NotNull Mono<ServerResponse> list(@NotNull ServerRequest request) {
    return webRequestSecurityService
        .isAuthorized(request)
        .flatMapMany(user -> simpleClientDao.findByOrganization(user.organization()))
        .collectList()
        .map(contacts -> contacts
            .stream()
            .map(ClientOutResource::from)
            .toArray(ClientOutResource[]::new))
        .flatMap(ResponseUtils::ok);
  }

  public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
    return RequestUtils
        .idRequest(request)
        .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
        .flatMap(authorized -> simpleClientDao.findByIdAndOrganization(authorized.getParam(), authorized.getOrgId()))
        .map(ClientOutResource::from)
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  public @NotNull Mono<ServerResponse> findPage(@NotNull ServerRequest request) {
    return RequestUtils
        .pageRequest(request)
        .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
        .flatMap(authorized -> {
          var pageRequest = authorized.getParam();
          return simpleClientDao.findPageByOrg(pageRequest.offset(), pageRequest.pageSize(), authorized.getOrgId());
        })
        .map(entityPage -> DataPageResponse.from(entityPage.totalSize(),
            entityPage.entities(),
            ClientOutResource::from,
            ClientOutResource[]::new))
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  private @NotNull Mono<? extends @NotNull SimpleClient> createContact(
      @NotNull AuthorizedResource<ClientInResource> authorized) {

    var user = authorized.getUser();
    var resource = authorized.getResource();

    return userDao
        .findByIdAndOrganization(resource.assigner(), user.organization())
        .zipWhen(assigner -> userDao
            .findByIdsAndOrganization(resource.curators(), assigner.organization())
            .collectList())
        .flatMap(args -> {

          var assigner = args.getT1();
          var curators = Set.copyOf(args.getT2());

          return simpleClientDao.create(
              assigner,
              curators,
              assigner.organization(),
              resource.firstName(),
              resource.secondName(),
              resource.thirdName(),
              DateUtils.toLocalDate(resource.birthday()),
              PhoneNumberResource.toPhoneNumbers(resource.phoneNumbers()),
              EmailResource.toEmails(resource.emails()),
              SiteResource.toSites(resource.sites()),
              MessengerResource.toMessengers(resource.messengers()),
              resource.company());
        });
  }

  private @NotNull Mono<? extends @NotNull SimpleClient> updateContact(
      @NotNull AuthorizedResource<ClientInResource> authorized) {

    var user = authorized.getUser();
    var org = user.organization();
    var resource = authorized.getResource();

    return simpleClientDao
        .findByIdAndOrganization(resource.id(), org)
        .switchIfEmpty(Mono.error(IdNotPresentedWebException::new))
        .zipWhen(contact -> {
          if (contact.version() != resource.version()) {
            return Mono.error(new ResourceIsAlreadyChangedWebException());
          } else {
            return userDao.findByIdAndOrganization(resource.assigner(), org);
          }
        })
        .switchIfEmpty(Mono.error(() -> ExceptionUtils.toBadRequest(
            ClientErrors.INVALID_ASSIGNER,
            ClientErrors.INVALID_ASSIGNER_MESSAGE)))
        .zipWhen(tuple -> userDao
            .findByIdsAndOrganization(resource.curators(), org)
            .collectList(), TupleUtils::merge)
        .flatMap(tuple -> {

          var contact = tuple.getT1();
          var assigner = tuple.getT2();
          var curators = tuple.getT3();

          contact.assignerId(assigner.id());
          contact.birthday(DateUtils.toLocalDate(resource.birthday()));
          contact.company(resource.company());
          contact.firstName(resource.firstName());
          contact.secondName(resource.secondName());
          contact.thirdName(resource.thirdName());
          contact.emails(EmailResource.toEmails(resource.emails()));
          contact.phoneNumbers(PhoneNumberResource.toPhoneNumbers(resource.phoneNumbers()));
          contact.sites(SiteResource.toSites(resource.sites()));
          contact.messengers(MessengerResource.toMessengers(resource.messengers()));
          contact.curatorIds(WithId.toIds(curators));

          return simpleClientDao
              .update(contact)
              .onErrorMap(NotActualObjectDaoException.class, ResourceIsAlreadyChangedWebException::new)
              .map(result -> contact);
        });
  }
}
