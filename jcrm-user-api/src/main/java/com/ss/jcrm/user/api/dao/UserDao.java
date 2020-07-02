package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CompletionException;

public interface UserDao extends Dao<User> {

    User[] EMPTY_USERS = new User[0];

    /**
     * @throws CompletionException -> DuplicateObjectDaoException if a user with the same name is already exist.
     */
    default @NotNull Mono<@NotNull User> create(
        @NotNull String email,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Organization organization,
        @NotNull Set<AccessRole> roles
    ) {
        return create(
            email,
            password,
            salt,
            organization,
            roles,
            Set.of(),
            Set.of()
        );
    }

    /**
     * @throws CompletionException -> DuplicateObjectDaoException if a user with the same name is already exist.
     */
    default @NotNull Mono<@NotNull User> create(
        @NotNull String email,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Organization organization,
        @NotNull Set<AccessRole> roles,
        @NotNull Set<PhoneNumber> phoneNumbers,
        @NotNull Set<Messenger> messengers
    ) {
        return create(
            email,
            password,
            salt,
            organization,
            roles,
            null,
            null,
            null,
            null,
            phoneNumbers,
            messengers
        );
    }

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
        @Nullable LocalDate birthday,
        @NotNull Set<PhoneNumber> phoneNumbers,
        @NotNull Set<Messenger> messengers
    );

    /**
     * @throws CompletionException -> NotActualObjectDaoException if the user was changed in another thread/server.
     */
    @NotNull Mono<Boolean> update(@NotNull User user);

    @NotNull Mono<Boolean> existByEmail(@NotNull String email);

    @NotNull Mono<@NotNull User> findByEmail(@NotNull String email);

    @NotNull Mono<@NotNull User> findByPhoneNumber(@NotNull String phoneNumber);

    @NotNull Mono<@NotNull Array<User>> searchByName(@NotNull String name, long orgId);

    default @NotNull Mono<@NotNull Array<User>> searchByName(@NotNull String name, @NotNull Organization organization) {
        return searchByName(name, organization.getId());
    }

    @NotNull Mono<@NotNull User> findByIdAndOrgId(long id, long orgId);

    default @NotNull Mono<@NotNull User> findByIdAndOrg(long id, @NotNull Organization organization) {
        return findByIdAndOrgId(id, organization.getId());
    }

    @NotNull Mono<@NotNull Array<User>> findByIdsAndOrgId(@Nullable long[] ids, long orgId);

    default @NotNull Mono<@NotNull Array<User>> findByIdsAndOrg(
        @Nullable long[] ids,
        @NotNull Organization organization
    ) {
        return findByIdsAndOrgId(ids, organization.getId());
    }

    @NotNull Mono<@NotNull EntityPage<User>> findPageByOrg(long offset, long size, long orgId);
}
