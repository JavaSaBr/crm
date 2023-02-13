package crm.client.api.dao;

import crm.dao.Dao;
import crm.dao.EntityPage;
import crm.dao.exception.NotActualObjectDaoException;
import crm.client.api.SimpleClient;
import crm.contact.api.Email;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.contact.api.Site;
import crm.user.api.Organization;
import crm.user.api.User;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface SimpleClientDao extends Dao<SimpleClient> {

  default @NotNull Mono<SimpleClient> create(
      @NotNull User assigner,
      @NotNull Organization organization,
      @NotNull String firstName,
      @NotNull String secondName,
      @NotNull String thirdName) {
    return create(
        assigner,
        Set.of(),
        organization,
        firstName,
        secondName,
        thirdName,
        null,
        Set.of(),
        Set.of(),
        Set.of(),
        Set.of(),
        null);
  }

  @NotNull Mono<SimpleClient> create(
      @NotNull User assigner,
      @NotNull Set<User> curators,
      @NotNull Organization organization,
      @Nullable String firstName,
      @Nullable String secondName,
      @Nullable String thirdName,
      @Nullable LocalDate birthday,
      @NotNull Set<PhoneNumber> phoneNumbers,
      @NotNull Set<Email> emails,
      @NotNull Set<Site> sites,
      @NotNull Set<Messenger> messengers,
      @Nullable String company);

  /**
   * @throws NotActualObjectDaoException if the contact was changed in another thread/server.
   */
  @NotNull Mono<SimpleClient> update(@NotNull SimpleClient contact);

  @NotNull Flux<SimpleClient> findByOrganization(long organizationId);

  default @NotNull Flux<SimpleClient> findByOrganization(@NotNull Organization organization) {
    return findByOrganization(organization.id());
  }

  @NotNull Mono<SimpleClient> findByIdAndOrganization(long id, long organizationId);

  default @NotNull Mono<SimpleClient> findByIdAndOrganization(long id, @NotNull Organization organization) {
    return findByIdAndOrganization(id, organization.id());
  }

  @NotNull Mono<EntityPage<SimpleClient>> findPageByOrg(long offset, long size, long orgId);
}
