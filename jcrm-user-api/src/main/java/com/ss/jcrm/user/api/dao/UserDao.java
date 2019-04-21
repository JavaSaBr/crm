package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface UserDao extends Dao<User> {

    /**
     * @throws DuplicateObjectDaoException if a user with the same name is already exist.
     */
    @NotNull User create(
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
     * @throws CompletionException -> DuplicateObjectDaoException if a user with the same name is already exist.
     */
    @NotNull CompletableFuture<@NotNull User> createAsync(
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
     * @throws NotActualObjectDaoException if the user was changed in another thread/server.
     */
    void update(@NotNull User user);

    /**
     * @throws CompletionException -> NotActualObjectDaoException if the user was changed in another thread/server.
     */
    @NotNull CompletableFuture<Void> updateAsync(@NotNull User user);

    boolean existByEmail(@NotNull String email);

    CompletableFuture<Boolean> existByEmailAsync(@NotNull String email);

    @Nullable User findByEmail(@NotNull String email);

    @NotNull CompletableFuture<@Nullable User> findByEmailAsync(@NotNull String email);

    @Nullable User findByPhoneNumber(@NotNull String phoneNumber);

    @NotNull CompletableFuture<@Nullable User> findByPhoneNumberAsync(@NotNull String phoneNumber);
}
