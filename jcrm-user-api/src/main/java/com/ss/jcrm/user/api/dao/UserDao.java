package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.CompletionException;

public interface UserDao extends Dao<User> {

    /**
     * @throws CompletionException -> DuplicateObjectDaoException if a user with the same name is already exist.
     */
    @NotNull Mono<@NotNull User> create(
        @NotNull String email,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Organization organization,
        @NotNull Set<AccessRole> roles,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @Nullable String phoneNumber
    );

    /**
     * @throws CompletionException -> NotActualObjectDaoException if the user was changed in another thread/server.
     */
    @NotNull Mono<Void> update(@NotNull User user);

    @NotNull Mono<Boolean> existByEmail(@NotNull String email);

    @NotNull Mono<@Nullable User> findByEmail(@NotNull String email);

    @NotNull Mono<@Nullable User> findByPhoneNumber(@NotNull String phoneNumber);

    @NotNull Mono<@NotNull Array<User>> searchByName(@NotNull String name, long orgId);

    default @NotNull Mono<@NotNull Array<User>> searchByName(@NotNull String name, @NotNull Organization organization) {
        return searchByName(name, organization.getId());
    }

    @NotNull Mono<@Nullable User> findByIdAndOrgId(long id, long orgId);

    default @NotNull Mono<@Nullable User> findByIdAndOrgId(long id, @NotNull Organization organization) {
        return findByIdAndOrgId(id, organization.getId());
    }

    @NotNull Mono<@NotNull Array<User>> findByIdsAndOrgId(@NotNull long[] ids, long orgId);

    default @NotNull Mono<@NotNull Array<User>> findByIdsAndOrgId(
        @NotNull long[] ids,
        @NotNull Organization organization
    ) {
        return findByIdsAndOrgId(ids, organization.getId());
    }
}
