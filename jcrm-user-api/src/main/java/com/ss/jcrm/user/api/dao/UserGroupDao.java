package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserGroupDao extends Dao<UserGroup> {

    /**
     * @throws DuplicateObjectDaoException if a group with the same name and organization is already exist
     */
    @NotNull Mono<@NotNull UserGroup> create(
        @NotNull String name,
        @NotNull Set<AccessRole> roles,
        @NotNull Organization organization
    );

    @NotNull Mono<@NotNull Set<UserGroup>> findAll(@NotNull Organization organization);

    @NotNull Mono<Boolean> existByName(@NotNull String email, long orgId);

    @NotNull Mono<@NotNull Array<UserGroup>> searchByName(@NotNull String name, long orgId);

    @NotNull Mono<@NotNull EntityPage<UserGroup>> findPageByOrg(long offset, long size, long orgId);

    @NotNull Mono<@NotNull UserGroup> findByIdAndOrgId(long id, long orgId);

    default @NotNull Mono<@NotNull UserGroup> findByIdAndOrg(long id, @NotNull Organization organization) {
        return findByIdAndOrgId(id, organization.getId());
    }

    @NotNull Mono<@NotNull Array<UserGroup>> findByIdsAndOrgId(@Nullable long[] ids, long orgId);

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
