package crm.user.api;

import crm.dao.ChangeTrackedEntity;
import crm.dao.VersionedUniqEntity;
import crm.security.AccessRole;
import crm.security.WithAccessRoles;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public interface User extends VersionedUniqEntity, ChangeTrackedEntity, WithAccessRoles, WithOrganization {

  @NotNull String email();
  @NotNull User email(@NotNull String email);

  @Nullable String firstName();
  @NotNull User firstName(@Nullable String firstName);

  @Nullable String secondName();
  @NotNull User secondName(@Nullable String secondName);

  @Nullable String thirdName();
  @NotNull User thirdName(@Nullable String thirdName);

  @Nullable LocalDate birthday();
  @NotNull User birthday(@Nullable LocalDate birthday);

  byte @NotNull [] password();
  @NotNull User password(byte @NotNull [] password);

  byte @NotNull [] salt();
  @NotNull User salt(byte @NotNull [] salt);

  @NotNull User roles(@NotNull Set<AccessRole> roles);

  @NotNull Set<UserGroup> groups();
  default @NotNull Stream<UserGroup> streamGroups() {
    return groups().stream();
  }
  @NotNull User groups(@NotNull Set<UserGroup> groups);

  default @NotNull User addGroup(@NotNull UserGroup userGroup) {

    var newGroups = new HashSet<>(groups());
    newGroups.add(userGroup);

    groups(Set.copyOf(newGroups));

    return this;
  }

  @NotNull Set<PhoneNumber> phoneNumbers();
  @NotNull User phoneNumbers(@NotNull Set<PhoneNumber> phoneNumbers);

  @NotNull Set<Messenger> messengers();
  @NotNull User messengers(@NotNull Set<Messenger> messengers);

  boolean emailConfirmed();
  @NotNull User emailConfirmed(boolean confirmed);

  int passwordVersion();
  @NotNull User passwordVersion(int passwordVersion);
}
