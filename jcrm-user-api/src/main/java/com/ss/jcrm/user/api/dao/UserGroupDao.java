package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface UserGroupDao extends Dao<UserGroup> {

    /**
     * @throws DuplicateObjectDaoException if a group with the same name and organization is already exist
     */
    @NotNull UserGroup create(@NotNull String name, @NotNull Organization organization);

    /**
     * @throws DuplicateObjectDaoException if a group with the same name and organization is already exist
     */
    @NotNull CompletableFuture<@NotNull UserGroup> createAsync(
        @NotNull String name,
        @NotNull Organization organization
    );

    @NotNull Set<UserGroup> getAll(@NotNull Organization organization);

    default @NotNull LongDictionary<UserGroup> getAllAsMap(@NotNull Organization organization) {

        var result = DictionaryFactory.<UserGroup>newLongDictionary();

        for (var group : getAll(organization)) {
            result.put(group.getId(), group);
        }

        return result;
    }

    @NotNull CompletableFuture<@NotNull Set<UserGroup>> getAllAsync(@NotNull Organization organization);

    @NotNull CompletableFuture<@NotNull LongDictionary<UserGroup>> getAllAsMapAsync(@NotNull Organization organization);
}
