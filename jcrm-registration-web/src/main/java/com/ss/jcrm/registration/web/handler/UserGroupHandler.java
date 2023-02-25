package com.ss.jcrm.registration.web.handler;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACCESS_ROLE;
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACCESS_ROLE_MESSAGE;
import static crm.base.web.util.ExceptionUtils.toBadRequest;
import static com.ss.rlib.common.function.Functions.Predicates.*;

import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.registration.web.resources.*;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import crm.security.AccessRole;
import crm.security.web.resource.AuthorizedParam;
import crm.security.web.resource.AuthorizedResource;
import crm.security.web.service.WebRequestSecurityService;
import crm.user.api.User;
import crm.user.api.UserGroup;
import crm.user.api.dao.MinimalUserDao;
import crm.user.api.dao.UserDao;
import crm.user.api.dao.UserGroupDao;
import crm.base.web.resources.DataPageResponse;
import crm.base.web.util.RequestUtils;
import crm.base.web.util.ResponseUtils;
import com.ss.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGroupHandler extends BaseRegistrationHandler {

  public static final AccessRole[] USER_GROUP_VIEWERS = {
      AccessRole.SUPER_ADMIN,
      AccessRole.ORG_ADMIN,
      AccessRole.USER_GROUP_MANAGER,
      AccessRole.VIEW_USER_GROUPS,
      AccessRole.CREATE_USER_GROUP,
      AccessRole.CHANGE_USER_GROUP,
      AccessRole.DELETE_USER_GROUP,
      };

  public static final AccessRole[] USER_GROUP_CREATORS = {
      AccessRole.SUPER_ADMIN,
      AccessRole.ORG_ADMIN,
      AccessRole.USER_GROUP_MANAGER,
      AccessRole.CREATE_USER_GROUP,
      };

  public static final AccessRole[] USER_GROUP_CHANGERS = {
      AccessRole.SUPER_ADMIN,
      AccessRole.ORG_ADMIN,
      AccessRole.USER_GROUP_MANAGER,
      AccessRole.CHANGE_USER_GROUP,
      };

  public static final AccessRole[] USER_GROUP_AND_USERS_VIEWERS =
      ArrayUtils.combineUniq(USER_GROUP_VIEWERS, UserHandler.USER_VIEWERS);

  @NotNull UserDao userDao;
  @NotNull UserGroupDao userGroupDao;
  @NotNull MinimalUserDao minimalUserDao;
  @NotNull ResourceValidator resourceValidator;
  @NotNull WebRequestSecurityService webRequestSecurityService;

  public @NotNull Mono<ServerResponse> existByName(@NotNull ServerRequest request) {
    return Mono
        .fromSupplier(() -> request.pathVariable("name"))
        .doOnNext(resourceValidator::validateUserGroupName)
        .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
        .flatMap(param -> userGroupDao.existByName(param.getParam(), param.getOrgId()))
        .flatMap(ResponseUtils::exist);
  }

  public @NotNull Mono<ServerResponse> findPage(@NotNull ServerRequest request) {
    return RequestUtils
        .pageRequest(request)
        .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
        .flatMap(authorized -> {
          var pageRequest = authorized.getParam();
          return userGroupDao.findPageByOrganization(pageRequest.offset(), pageRequest.pageSize(), authorized.getOrgId());
        })
        .map(page -> DataPageResponse.from(page.totalSize(), page.entities(), UserGroupOutResource::from))
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  public @NotNull Mono<ServerResponse> findUsersPage(@NotNull ServerRequest request) {
    return RequestUtils
        .idBasedPageRequest(request)
        .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_AND_USERS_VIEWERS), AuthorizedParam::new)
        .flatMap(authorized -> {
          var pageRequest = authorized.getParam();
          return minimalUserDao.findPageByOrganizationAndGroup(pageRequest.offset(),
              pageRequest.pageSize(),
              authorized.getOrgId(),
              pageRequest.id());
        })
        .map(page -> DataPageResponse.from(page.totalSize(), page.entities(), MinimalUserOutResource::from))
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
    return RequestUtils
        .idRequest(request)
        .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
        .flatMap(param -> userGroupDao.findByIdAndOrganization(param.getParam(), param.getOrgId()))
        .map(UserGroupOutResource::from)
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  public @NotNull Mono<ServerResponse> findByIds(@NotNull ServerRequest request) {
    return RequestUtils
        .idsRequest(request)
        .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
        .flatMap(param -> userGroupDao
            .findByIdsAndOrganization(param.getParam(), param.getOrgId())
            .collectList())
        .map(users -> users
            .stream()
            .map(UserGroupOutResource::from)
            .toArray(UserGroupOutResource[]::new))
        .flatMap(ResponseUtils::ok)
        .switchIfEmpty(ResponseUtils.lazyNotFound());
  }

  public @NotNull Mono<ServerResponse> searchByName(@NotNull ServerRequest request) {
    return Mono
        .fromSupplier(() -> request.pathVariable("name"))
        .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
        .flatMap(res -> userGroupDao
            .searchByName(res.getParam(), res.getOrgId())
            .collectList())
        .map(users -> users
            .stream()
            .map(UserGroupOutResource::from)
            .toArray(UserGroupOutResource[]::new))
        .flatMap(ResponseUtils::ok);
  }

  public @NotNull Mono<ServerResponse> update(@NotNull ServerRequest request) {
    return webRequestSecurityService
        .isAuthorized(request, USER_GROUP_CHANGERS)
        .zipWhen(user -> request.bodyToMono(UserGroupInResource.class), AuthorizedResource::new)
        .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
        .flatMap(this::updateUserGroup)
        .map(UserGroupOutResource::from)
        .flatMap(ResponseUtils::ok);
  }

  public @NotNull Mono<ServerResponse> create(@NotNull ServerRequest request) {
    return webRequestSecurityService
        .isAuthorized(request, USER_GROUP_CREATORS)
        .zipWhen(user -> request.bodyToMono(UserGroupInResource.class), AuthorizedResource::new)
        .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
        .flatMap(this::createUserGroup)
        .map(UserGroupOutResource::from)
        .flatMap(ResponseUtils::created);
  }

  private @NotNull Mono<? extends @NotNull UserGroup> updateUserGroup(
      @NotNull AuthorizedResource<UserGroupInResource> authorized) {

    var creator = authorized.getUser();
    var organization = creator.organization();

    var resource = authorized.getResource();
    var roles = AccessRole.toSet(resource.roles());

    verifyRoles(creator, roles);

    return userGroupDao
        .findByIdAndOrganization(resource.id(), organization.id())
        .switchIfEmpty(Mono.error(() -> toBadRequest(RegistrationErrors.USER_GROUP_IS_NOT_EXIST,
            RegistrationErrors.USER_GROUP_IS_NOT_EXIST_MESSAGE)))
        .flatMap(userGroupDao::update);
  }

  private @NotNull Mono<? extends @NotNull UserGroup> createUserGroup(
      @NotNull AuthorizedResource<UserGroupInResource> authorized) {

    var creator = authorized.getUser();
    var organization = creator.organization();

    var resource = authorized.getResource();
    var roles = AccessRole.toSet(resource.roles());

    verifyRoles(creator, roles);

    return userGroupDao
        .existByName(resource.name(), organization.id())
        .filter(throwIfTrue(() -> toBadRequest(RegistrationErrors.USER_GROUP_IS_ALREADY_EXIST,
            RegistrationErrors.USER_GROUP_IS_ALREADY_EXIST_MESSAGE)))
        .flatMap(skip -> userGroupDao.create(resource.name(), roles, organization));
  }

  private void verifyRoles(@NotNull User creator, @NotNull Set<AccessRole> roles) {

    if (roles.isEmpty()) {
      return;
    }

    var ownedRoles = webRequestSecurityService.resolveAllRoles(creator);

    if (!AccessRole.canAssignRoles(ownedRoles, roles)) {
      throw toBadRequest(INVALID_ACCESS_ROLE, INVALID_ACCESS_ROLE_MESSAGE);
    }
  }
}
