package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.user.api.Organization;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;

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

    @NotNull Array<Organization> getAll();

    @NotNull CompletableFuture<@NotNull Array<Organization>> getAllAsync();

    boolean existByName(@NotNull String name);

    @NotNull CompletableFuture<Boolean> existByNameAsync(@NotNull String name);

    boolean delete(long id);

    @NotNull CompletableFuture<Boolean> deleteAsync(long id);
}
