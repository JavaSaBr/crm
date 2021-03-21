package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.registration.web.exception.RegistrationErrors;
import com.ss.jcrm.registration.web.resources.MinimalUserOutResource;
import com.ss.jcrm.registration.web.resources.UserInResource;
import com.ss.jcrm.registration.web.resources.UserOutResource;
import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.service.PasswordService;
import com.ss.jcrm.security.web.resource.AuthorizedParam;
import com.ss.jcrm.security.web.resource.AuthorizedResource;
import com.ss.jcrm.security.web.service.WebRequestSecurityService;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.web.exception.ExceptionUtils;
import com.ss.jcrm.web.resources.DataPageResponse;
import com.ss.jcrm.web.util.RequestUtils;
import com.ss.jcrm.web.util.ResponseUtils;
import com.ss.rlib.common.util.DateUtils;
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
public class UserHandler extends BaseRegistrationHandler {

    public static final AccessRole[] USER_VIEWERS = {
        AccessRole.SUPER_ADMIN,
        AccessRole.ORG_ADMIN,
        AccessRole.USER_MANAGER,
        AccessRole.VIEW_USERS
    };

    public static final AccessRole[] USER_CREATORS = {
        AccessRole.SUPER_ADMIN,
        AccessRole.ORG_ADMIN,
        AccessRole.USER_MANAGER,
    };

    @NotNull UserDao userDao;
    @NotNull ResourceValidator resourceValidator;
    @NotNull WebRequestSecurityService webRequestSecurityService;
    @NotNull PasswordService passwordService;

    public @NotNull Mono<ServerResponse> existByEmail(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("email"))
            .doOnNext(resourceValidator::validateEmail)
            .flatMap(userDao::existByEmail)
            .flatMap(ResponseUtils::exist);
    }

    public @NotNull Mono<ServerResponse> findMinimalById(@NotNull ServerRequest request) {
        return RequestUtils.idRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(param -> userDao.findByIdAndOrgId(param.getParam(), param.getOrgId()))
            .map(MinimalUserOutResource::from)
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findMinimalByIds(@NotNull ServerRequest request) {
        return RequestUtils.idsRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(param -> userDao.findByIdsAndOrgId(param.getParam(), param.getOrgId()))
            .map(users -> users.stream()
                .map(MinimalUserOutResource::from)
                .toArray(MinimalUserOutResource[]::new))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> searchByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .zipWith(webRequestSecurityService.isAuthorized(request), AuthorizedParam::new)
            .flatMap(res -> userDao.searchByName(res.getParam(), res.getOrgId()))
            .map(users -> users.stream()
                .map(MinimalUserOutResource::from)
                .toArray(MinimalUserOutResource[]::new))
            .flatMap(ResponseUtils::ok);
    }

    public @NotNull Mono<ServerResponse> findPage(@NotNull ServerRequest request) {
        return RequestUtils.pageRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_VIEWERS), AuthorizedParam::new)
            .flatMap(authorized -> {
                var pageRequest = authorized.getParam();
                return userDao.findPageByOrg(
                    pageRequest.offset(),
                    pageRequest.pageSize(),
                    authorized.getOrgId()
                );
            })
            .map(entityPage -> DataPageResponse.from(
                entityPage.totalSize(),
                entityPage.entities(),
                UserOutResource::from,
                UserOutResource[]::new
            ))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findById(@NotNull ServerRequest request) {
        return RequestUtils.idRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_VIEWERS), AuthorizedParam::new)
            .flatMap(param -> userDao.findByIdAndOrgId(param.getParam(), param.getOrgId()))
            .map(UserOutResource::from)
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> findByIds(@NotNull ServerRequest request) {
        return RequestUtils.idsRequest(request)
            .zipWith(webRequestSecurityService.isAuthorized(request, USER_VIEWERS), AuthorizedParam::new)
            .flatMap(param -> userDao.findByIdsAndOrgId(param.getParam(), param.getOrgId()))
            .map(users -> users.stream()
                .map(UserOutResource::from)
                .toArray(UserOutResource[]::new))
            .flatMap(ResponseUtils::ok)
            .switchIfEmpty(ResponseUtils.lazyNotFound());
    }

    public @NotNull Mono<ServerResponse> create(@NotNull ServerRequest request) {
        return webRequestSecurityService.isAuthorized(request, USER_CREATORS)
            .zipWhen(user -> request.bodyToMono(UserInResource.class), AuthorizedResource::new)
            .doOnNext(authorized -> resourceValidator.validate(authorized.getResource()))
            .flatMap(this::createUser)
            .map(UserOutResource::from)
            .flatMap(ResponseUtils::created);
    }

    private @NotNull Mono<? extends @NotNull User> createUser(@NotNull AuthorizedResource<UserInResource> authorized) {

        var creator = authorized.getUser();
        var resource = authorized.getResource();
        var birthday = DateUtils.toLocalDate(resource.birthday());

        var salt = passwordService.getNextSalt();
        var hash = passwordService.hash(resource.password(), salt);

        //noinspection ConstantConditions
        return userDao.existByEmail(resource.email())
            .filter(emailAlreadyExist -> !emailAlreadyExist)
            .flatMap(emailAlreadyExist -> userDao.create(
                resource.email(),
                hash,
                salt,
                creator.getOrganization(),
                Set.of(),
                resource.firstName(),
                resource.secondName(),
                resource.thirdName(),
                birthday,
                toPhoneNumbers(resource.phoneNumbers()),
                toMessengers(resource.messengers())
            ))
            .switchIfEmpty(Mono.error(() -> ExceptionUtils.toBadRequest(
                RegistrationErrors.EMAIL_ALREADY_EXIST,
                RegistrationErrors.EMAIL_ALREADY_EXIST_MESSAGE
            )));
    }
}
