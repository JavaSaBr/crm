package crm.client.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import crm.client.api.ClientEmail;
import crm.client.api.ClientMessenger;
import crm.client.api.ClientPhoneNumber;
import crm.client.api.ClientSite;
import crm.client.api.SimpleClient;
import crm.user.api.Organization;
import crm.user.api.User;
import java.util.List;
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
        null,
        organization,
        firstName,
        secondName,
        thirdName,
        null,
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        null);
  }

  @NotNull Mono<SimpleClient> create(
      @NotNull User assigner,
      @Nullable List<User> curators,
      @NotNull Organization organization,
      @NotNull String firstName,
      @NotNull String secondName,
      @Nullable String thirdName,
      @Nullable LocalDate birthday,
      @NotNull List<ClientPhoneNumber> phoneNumbers,
      @NotNull List<ClientEmail> emails,
      @NotNull List<ClientSite> sites,
      @NotNull List<ClientMessenger> messengers,
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
