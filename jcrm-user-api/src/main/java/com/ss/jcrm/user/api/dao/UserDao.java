package com.ss.jcrm.user.api.dao;

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

public interface UserDao extends NamedObjectDao<User> {

    /**
     * @throws DuplicateObjectDaoException if a user with the same name is already exist.
     */
    @NotNull User create(
        @NotNull String name,
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
        @NotNull String name,
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

    boolean existByName(@NotNull String name);

    CompletableFuture<Boolean> existByNameAsync(@NotNull String name);
}
