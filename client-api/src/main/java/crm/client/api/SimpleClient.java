package crm.client.api;

import com.ss.jcrm.dao.VersionedUniqEntity;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

public interface SimpleClient extends VersionedUniqEntity {

  long assignerId();
  SimpleClient assignerId(long assignerId);

  long @NotNull [] curatorIds();
  SimpleClient curatorIds(long @NotNull [] curatorIds);

  long organizationId();
  SimpleClient organizationId(long organizationId);

  @Nullable String firstName();
  SimpleClient firstName(@Nullable String firstName);

  @Nullable String secondName();
  SimpleClient secondName(@Nullable String secondName);

  @Nullable String thirdName();
  SimpleClient thirdName(@Nullable String thirdName);

  @Nullable LocalDate birthday();
  SimpleClient birthday(@Nullable LocalDate birthday);

  @NotNull List<ClientPhoneNumber> phoneNumbers();
  SimpleClient phoneNumbers(@NotNull List<ClientPhoneNumber> phoneNumbers);

  @NotNull List<ClientEmail> emails();
  SimpleClient emails(@NotNull List<ClientEmail> emails);

  @NotNull List<ClientSite> sites();
  SimpleClient sites(@NotNull List<ClientSite> sites);

  @NotNull List<ClientMessenger> messengers();
  SimpleClient messengers(@NotNull List<ClientMessenger> messengers);

  @Nullable String company();
  SimpleClient company(@Nullable String company);

  @NotNull Instant created();
  SimpleClient created(@NotNull Instant created);

  @NotNull Instant modified();
  SimpleClient modified(@NotNull Instant modified);
}
