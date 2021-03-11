package com.ss.jcrm.registration.web.handler;

import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACCESS_ROLE;
import static com.ss.jcrm.registration.web.exception.RegistrationErrors.INVALID_ACCESS_ROLE_MESSAGE;
import static com.ss.jcrm.web.exception.ExceptionUtils.toBadRequest;
import static com.ss.rlib.common.function.Functions.Predicates.*;
import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.registration.web.resources.*;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.web.resource.AuthorizedParam;
import com.ss.jcrm.security.web.resource.AuthorizedResource;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.jcrm.user.api.dao.MinimalUserDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.web.resources.DataPageResponse;
import com.ss.jcrm.web.util.RequestUtils;
import com.ss.jcrm.web.util.ResponseUtils;
import com.ss.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
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

    public static final AccessRole[] USER_GROUP_AND_USERS_VIEWERS = ArrayUtils.combineUniq(
        USER_GROUP_VIEWERS,
        UserHandler.USER_VIEWERS
    );

    @NotNull UserDao userDao;
    @NotNull UserGroupDao userGroupDao;
    @NotNull MinimalUserDao minimalUserDao;
    @NotNull ResourceValidator resourceValidator;
    @NotNull WebRequestSecurityService webRequestSecurityService;

    public @NotNull Mono<ServerResponse> existByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .doOnNext(resourceValidator::validateUserGroupName)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
            .flatMap(param -> userGroupDao.existByName(param.getParam(), param.getOrgId()))
            .flatMap(ResponseUtils::exist);
    }

    public @NotNull Mono<ServerResponse> findPage(@NotNull ServerRequest request) {
        return RequestUtils.pageRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
            .flatMap(authorized -> {
                var pageRequest = authorized.getParam();
                return userGroupDao.findPageByOrg(
                    pageRequest.getOffset(),
                    pageRequest.getPageSize(),
                    authorized.getOrgId()
                );
            })
            .map(entityPage -> DataPageResponse.from(
                entityPage.getTotalSize(),
                entityPage.getEntities(),
                UserGroupOutResource::new,
                UserGroupOutResource[]::new
            ))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findUsersPage(@NotNull ServerRequest request) {
        return RequestUtils.idBasedPageRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_AND_USERS_VIEWERS), AuthorizedParam::new)
            .flatMap(authorized -> {
                var pageRequest = authorized.getParam();
                return minimalUserDao.findPageByOrgAndGroup(
                    pageRequest.getOffset(),
                    pageRequest.getPageSize(),
                    authorized.getOrgId(),
                    pageRequest.getId()
                );
            })
            .map(entityPage -> DataPageResponse.from(
                entityPage.getTotalSize(),
                entityPage.getEntities(),
                MinimalUserOutResource::new,
                MinimalUserOutResource[]::new
            ))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
        return RequestUtils.idRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
            .flatMap(param -> userGroupDao.findByIdAndOrgId(param.getParam(), param.getOrgId()))
            .map(UserGroupOutResource::new)
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findByIds(@NotNull ServerRequest request) {
        return RequestUtils.idsRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
            .flatMap(param -> userGroupDao.findByIdsAndOrgId(param.getParam(), param.getOrgId()))
            .map(users -> users.stream()
                .map(UserGroupOutResource::new)
                .toArray(UserGroupOutResource[]::new))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> searchByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_GROUP_VIEWERS), AuthorizedParam::new)
            .flatMap(res -> userGroupDao.searchByName(res.getParam(), res.getOrgId()))
            .map(users -> users.stream()
                .map(UserGroupOutResource::new)
                .toArray(UserGroupOutResource[]::new))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> create(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, USER_GROUP_CREATORS)
            .zipWhen(user -> request.bodyToMono(UserGroupInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::createUserGroup)
            .map(UserGroupOutResource::new)
            .flatMap(ResponseUtils::created);
    }

    private @NotNull Mono<? extends @NotNull UserGroup> createUserGroup(
        @NotNull AuthorizedResource<UserGroupInResource> authorized
    ) {

        var creator = authorized.getUser();
        var organization = creator.getOrganization();

        var resource = authorized.getResource();
        var users = resource.getUsers();

        var roles = AccessRole.toSet(resource.getRoles());

        verifyRoles(creator, roles);

        return userGroupDao.existByName(resource.getName(), organization.getId())
            .filter(throwIfTrue(() -> toBadRequest(
                RegistrationErrors.USER_GROUP_IS_ALREDY_EXIST,
                RegistrationErrors.USER_GROUP_IS_ALREDY_EXIST_MESSAGE
            )))
            .flatMap(skip -> userDao.containsAll(users, organization.getId()))
            .filter(throwIfFalse(() -> toBadRequest(
                RegistrationErrors.INVALID_USER_LIST,
                RegistrationErrors.INVALID_USER_LIST_MESSAGE
            )))
            .flatMap(skip -> userGroupDao.create(
                resource.getName(),
                roles,
                organization
            ))
            .flatMap(userGroup -> userDao.findByIdsAndOrgId(users, organization.getId())
                .flatMapMany(Flux::fromIterable)
                .doOnNext(user -> user.addGroup(userGroup))
                .flatMap(userDao::update)
                .last()
                .map(user -> userGroup));
    }

    private void verifyRoles(@NotNull User creator, @NotNull Set<AccessRole> roles) {

        if (roles.isEmpty()) {
            return;
        }

        var ownedRoles = webRequestSecurityService.resolveAllRoles(creator);

        if(!AccessRole.canAssignRoles(ownedRoles, roles)) {
            throw toBadRequest(
                INVALID_ACCESS_ROLE,
                INVALID_ACCESS_ROLE_MESSAGE
            );
        }
    }
}
