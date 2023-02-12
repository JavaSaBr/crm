package crm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException;
import com.ss.jcrm.security.AccessRole;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.user.api.Organization;
import crm.user.api.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CompletionException;

public interface UserDao extends Dao<User> {

  /**
   * @throws DuplicateObjectDaoException if a user with the same name is already exist.
   */
  default @NotNull Mono<User> create(
      @NotNull String email,
      byte @NotNull [] password,
      byte @NotNull [] salt,
      @NotNull Organization organization,
      @NotNull Set<AccessRole> roles) {
    return create(email, password, salt, organization, roles, Set.of(), Set.of());
  }

  /**
   * @throws CompletionException -> DuplicateObjectDaoException if a user with the same name is already exist.
   */
  default @NotNull Mono<User> create(
      @NotNull String email,
      byte @NotNull [] password,
      byte @NotNull [] salt,
      @NotNull Organization organization,
      @NotNull Set<AccessRole> roles,
      @NotNull Set<PhoneNumber> phoneNumbers,
      @NotNull Set<Messenger> messengers) {
    return create(email, password, salt, organization, roles, null, null, null, null, phoneNumbers, messengers);
  }

  /**
   * @throws CompletionException -> DuplicateObjectDaoException if a user with the same name is already exist.
   */
  @NotNull Mono<@NotNull User> create(
      @NotNull String email,
      byte @NotNull [] password,
      byte @NotNull [] salt,
      @NotNull Organization organization,
      @NotNull Set<AccessRole> roles,
      @Nullable String firstName,
      @Nullable String secondName,
      @Nullable String thirdName,
      @Nullable LocalDate birthday,
      @NotNull Set<PhoneNumber> phoneNumbers,
      @NotNull Set<Messenger> messengers);

  /**
   * @throws CompletionException -> NotActualObjectDaoException if the user was changed in another thread/server.
   */
  @NotNull Mono<User> update(@NotNull User user);

  @NotNull Mono<Boolean> existByEmail(@NotNull String email);

  @NotNull Mono<User> findByEmail(@NotNull String email);

  @NotNull Mono<User> findByPhoneNumber(@NotNull String phoneNumber);

  @NotNull Flux<User> searchByName(@NotNull String name, long orgId);

  default @NotNull Flux<User> searchByName(@NotNull String name, @NotNull Organization organization) {
    return searchByName(name, organization.id());
  }

  @NotNull Mono<User> findByIdAndOrganization(long id, long organizationId);

  default @NotNull Mono<User> findByIdAndOrganization(long id, @NotNull Organization organization) {
    return findByIdAndOrganization(id, organization.id());
  }

  @NotNull Flux<User> findByIdsAndOrganization(long @Nullable [] ids, long organizationId);

  default @NotNull Flux<User> findByIdsAndOrganization(long @Nullable [] ids, @NotNull Organization organization) {
    return findByIdsAndOrganization(ids, organization.id());
  }

  @NotNull Mono<EntityPage<User>> findPageByOrganization(long offset, long size, long organizationId);

  @NotNull Mono<Boolean> containsAll(long @NotNull [] ids, long organizationId);
}
