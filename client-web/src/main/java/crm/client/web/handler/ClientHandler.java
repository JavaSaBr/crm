package crm.client.web.handler;

import crm.base.util.WithId;
import crm.client.api.dao.SimpleClientDao;
import crm.dao.exception.NotActualObjectDaoException;
import crm.security.AccessRole;
import crm.security.web.resource.AuthorizedParam;
import crm.security.web.resource.AuthorizedResource;
import crm.security.web.service.WebRequestSecurityService;
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
import crm.client.web.exception.ClientErrors;
import crm.client.web.resource.*;
import crm.client.web.validator.ResourceValidator;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
        .flatMap(this::createClient)
        .map(ClientOutResource::from)
        .flatMap(ResponseUtils::created);
  }

  public @NotNull Mono<ServerResponse> update(@NotNull ServerRequest request) {
    return webRequestSecurityService
        .isAuthorized(request, AccessRole.ORG_ADMIN)
        .zipWhen(user -> request.bodyToMono(ClientInResource.class), AuthorizedResource::new)
        .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
        .flatMap(this::updateClient)
        .map(ClientOutResource::from)
        .flatMap(ResponseUtils::ok);
  }

  public @NotNull Mono<ServerResponse> list(@NotNull ServerRequest request) {
    return webRequestSecurityService
        .isAuthorized(request)
        .flatMapMany(user -> simpleClientDao.findByOrganization(user.organization()))
        .collectList()
        .map(clients -> clients
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
        .map(page -> DataPageResponse.from(page.totalSize(), page.entities(), ClientOutResource::from))
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  private @NotNull Mono<SimpleClient> createClient(@NotNull AuthorizedResource<ClientInResource> authorized) {

    var user = authorized.getUser();
    var resource = authorized.getResource();

    return userDao
        .findByIdAndOrganization(resource.assigner(), user.organization())
        .zipWhen(assigner -> userDao
            .findByIdsAndOrganization(resource.curators(), assigner.organization())
            .collectList())
        .flatMap(assignerAndCurators -> {

          var assigner = assignerAndCurators.getT1();
          var curators = Set.copyOf(assignerAndCurators.getT2());

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

  private @NotNull Mono<SimpleClient> updateClient(@NotNull AuthorizedResource<ClientInResource> authorized) {

    var user = authorized.getUser();
    var organization = user.organization();
    var resource = authorized.getResource();

    return simpleClientDao
        .findByIdAndOrganization(resource.id(), organization)
        .switchIfEmpty(Mono.error(IdNotPresentedWebException::new))
        .zipWhen(client -> {
          if (client.version() != resource.version()) {
            return Mono.error(new ResourceIsAlreadyChangedWebException());
          } else {
            return userDao.findByIdAndOrganization(resource.assigner(), organization);
          }
        })
        .switchIfEmpty(Mono.error(() -> ExceptionUtils.toBadRequest(
            ClientErrors.INVALID_ASSIGNER,
            ClientErrors.INVALID_ASSIGNER_MESSAGE)))
        .zipWhen(clientAndAssigner -> userDao
            .findByIdsAndOrganization(resource.curators(), organization)
            .collectList(), TupleUtils::merge)
        .flatMap(clientAssignerAndCurators -> {

          var client = clientAssignerAndCurators.getT1();
          var assigner = clientAssignerAndCurators.getT2();
          var curators = clientAssignerAndCurators.getT3();

          client.assignerId(assigner.id());
          client.birthday(DateUtils.toLocalDate(resource.birthday()));
          client.company(resource.company());
          client.firstName(resource.firstName());
          client.secondName(resource.secondName());
          client.thirdName(resource.thirdName());
          client.emails(EmailResource.toEmails(resource.emails()));
          client.phoneNumbers(PhoneNumberResource.toPhoneNumbers(resource.phoneNumbers()));
          client.sites(SiteResource.toSites(resource.sites()));
          client.messengers(MessengerResource.toMessengers(resource.messengers()));
          client.curatorIds(WithId.toIds(curators));

          return simpleClientDao
              .update(client)
              .onErrorMap(NotActualObjectDaoException.class, ResourceIsAlreadyChangedWebException::new)
              .map(result -> client);
        });
  }
}
