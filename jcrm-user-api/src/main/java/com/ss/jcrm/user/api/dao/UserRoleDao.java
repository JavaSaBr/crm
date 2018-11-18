package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.user.api.UserRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface UserRoleDao extends NamedObjectDao<UserRole> {

    /**
     * @throws DuplicateObjectDaoException if a role with the same name is already exist/
     */
    @NotNull UserRole create(@NotNull String name);

    /**
     * @throws DuplicateObjectDaoException if a role with the same name is already exist/
     */
    @NotNull CompletableFuture<@NotNull UserRole> createAsync(@NotNull String name);

    @NotNull Set<UserRole> getAll();

    @NotNull CompletableFuture<@NotNull Set<UserRole>> getAllAsync();
}
