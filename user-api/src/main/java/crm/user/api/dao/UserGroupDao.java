package crm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
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
      @NotNull String name, @NotNull Set<AccessRole> roles, @NotNull Organization organization);

  @NotNull Mono<UserGroup> update(@NotNull UserGroup userGroup);

  @NotNull Flux<UserGroup> findAll(@NotNull Organization organization);

  @NotNull Mono<Boolean> existByName(@NotNull String name, long orgId);

  @NotNull Flux<UserGroup> searchByName(@NotNull String name, long orgId);

  @NotNull Mono<EntityPage<UserGroup>> findPageByOrg(long offset, long size, long orgId);

  @NotNull Mono<UserGroup> findByIdAndOrgId(long id, long orgId);

  default @NotNull Mono<UserGroup> findByIdAndOrg(long id, @NotNull Organization organization) {
    return findByIdAndOrgId(id, organization.id());
  }

  @NotNull Flux<UserGroup> findByIdsAndOrgId(long @Nullable [] ids, long orgId);

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
