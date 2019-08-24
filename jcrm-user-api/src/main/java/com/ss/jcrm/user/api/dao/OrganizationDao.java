package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.NamedObjectDao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.user.api.Organization;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface OrganizationDao extends NamedObjectDao<Organization> {

    /**
     * @throws DuplicateObjectDaoException if an organization with the same name is already exist.
     */
    @NotNull Mono<@NotNull Organization> create(@NotNull String name, @NotNull Country country);

    @NotNull Mono<@NotNull Array<Organization>> findAll();

    @NotNull Mono<Boolean> existByName(@NotNull String name);

    @NotNull Mono<Boolean> delete(long id);
}
