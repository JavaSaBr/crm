package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.user.api.Organization;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrganizationDao extends NamedObjectDao<Organization> {

    /**
     * @throws DuplicateObjectDaoException if an organization with the same name is already exist.
     */
    @NotNull Organization create(@NotNull String name, @NotNull Country country);

    /**
     * @throws DuplicateObjectDaoException if an organization with the same name is already exist.
     */
    @NotNull CompletableFuture<@NotNull Organization> createAsync(@NotNull String name, @NotNull Country country);

    @NotNull List<Organization> getAll();

    @NotNull CompletableFuture<@NotNull List<Organization>> getAllAsync();

    boolean existByName(@NotNull String name);

    @NotNull CompletableFuture<Boolean> existByNameAsync(@NotNull String name);

    boolean delete(long id);

    @NotNull CompletableFuture<Boolean> deleteAsync(long id);
}
