package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface UserDao extends NamedObjectDao<User> {

    /**
     * @throws DuplicateObjectDaoException if a user with the same name is already exist.
     */
    @NotNull User create(
        @NotNull String name,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @Nullable Organization organization
    );

    /**
     * @throws DuplicateObjectDaoException if a user with the same name is already exist.
     */
    @NotNull CompletableFuture<@NotNull User> createAsync(
        @NotNull String name,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @Nullable Organization organization
    );

    /**
     * @throws NotActualObjectDaoException if the user was changed in another thread/server.
     */
    void addRole(@NotNull User user, @NotNull UserRole role);

    /**
     * @throws NotActualObjectDaoException if the user was changed in another thread/server.
     */
    @NotNull CompletableFuture<Void> addRoleAsync(@NotNull User user, @NotNull UserRole role);

    /**
     * @throws NotActualObjectDaoException if the user was changed in another thread/server.
     */
    void removeRole(@NotNull User user, @NotNull UserRole role);

    /**
     * @throws NotActualObjectDaoException if the user was changed in another thread/server.
     */
    @NotNull CompletableFuture<Void> removeRoleAsync(@NotNull User user, @NotNull UserRole role);
}
