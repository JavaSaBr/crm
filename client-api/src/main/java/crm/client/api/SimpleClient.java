package crm.client.api;

import crm.dao.VersionedUniqEntity;
import crm.contact.api.Email;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.contact.api.Site;
import java.util.Set;
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

  @NotNull Set<PhoneNumber> phoneNumbers();
  SimpleClient phoneNumbers(@NotNull Set<PhoneNumber> phoneNumbers);

  @NotNull Set<Email> emails();
  SimpleClient emails(@NotNull Set<Email> emails);

  @NotNull Set<Site> sites();
  SimpleClient sites(@NotNull Set<Site> sites);

  @NotNull Set<Messenger> messengers();
  SimpleClient messengers(@NotNull Set<Messenger> messengers);

  @Nullable String company();
  SimpleClient company(@Nullable String company);

  @NotNull Instant created();
  SimpleClient created(@NotNull Instant created);

  @NotNull Instant modified();
  SimpleClient modified(@NotNull Instant modified);
}
