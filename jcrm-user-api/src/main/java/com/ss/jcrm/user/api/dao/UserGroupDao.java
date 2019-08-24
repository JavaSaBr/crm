package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserGroupDao extends Dao<UserGroup> {

    /**
     * @throws DuplicateObjectDaoException if a group with the same name and organization is already exist
     */
    @NotNull Mono<@NotNull UserGroup> create(@NotNull String name, @NotNull Organization organization);

    @NotNull Mono<@NotNull Set<UserGroup>> findAll(@NotNull Organization organization);

    default @NotNull Mono<@NotNull LongDictionary<UserGroup>> findAllAsMap(@NotNull Organization organization) {

        return findAll(organization)
            .map(userGroups -> {

                var result = DictionaryFactory.<UserGroup>newLongDictionary();

                for (var group : userGroups) {
                    result.put(group.getId(), group);
                }

                return result;
            });
    }
}
