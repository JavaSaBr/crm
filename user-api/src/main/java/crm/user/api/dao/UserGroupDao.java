package crm.user.api.dao;

import crm.dao.Dao;
import crm.dao.EntityPage;
import crm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.security.AccessRole;
import crm.user.api.Organization;
import crm.user.api.UserGroup;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserGroupDao extends Dao<UserGroup> {

  /**
   * @throws DuplicateObjectDaoException if a group with the same name and organization is already exist
   */
  @NotNull Mono<UserGroup> create(
      @NotNull String name,
      @NotNull Set<AccessRole> roles,
      @NotNull Organization organization);

  @NotNull Mono<UserGroup> update(@NotNull UserGroup userGroup);

  @NotNull Flux<UserGroup> findAll(@NotNull Organization organization);

  @NotNull Mono<Boolean> existByName(@NotNull String name, long organizationId);

  @NotNull Flux<UserGroup> searchByName(@NotNull String name, long organizationId);

  @NotNull Mono<EntityPage<UserGroup>> findPageByOrganization(long offset, long size, long organizationId);

  @NotNull Mono<UserGroup> findByIdAndOrganization(long id, long organizationId);

  default @NotNull Mono<UserGroup> findByIdAndOrganization(long id, @NotNull Organization organization) {
    return findByIdAndOrganization(id, organization.id());
  }

  @NotNull Flux<UserGroup> findByIdsAndOrganization(long @Nullable [] ids, long organizationId);

  default @NotNull Mono<LongDictionary<UserGroup>> findAllAsMap(@NotNull Organization organization) {
    return findAll(organization)
        .collectList()
        .map(userGroups -> {

          var result = DictionaryFactory.<UserGroup>newLongDictionary();

          for (var group : userGroups) {
            result.put(group.id(), group);
          }

          return result;
        });
  }
}
