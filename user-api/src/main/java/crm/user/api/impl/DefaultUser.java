package crm.user.api.impl;

import crm.security.AccessRole;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.user.api.Organization;
import crm.user.api.User;
import crm.user.api.UserGroup;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@ToString(of = {
    "id",
    "email",
    "organization"
})
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Accessors(fluent = true, chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultUser implements User {

  final long id;

  final @NotNull Organization organization;
  final @NotNull Instant created;

  volatile @NotNull String email;

  volatile @Nullable String firstName;
  volatile @Nullable String secondName;
  volatile @Nullable String thirdName;
  volatile @Nullable LocalDate birthday;

  volatile @NotNull Instant modified;

  volatile byte @NotNull [] password;
  volatile byte @NotNull [] salt;

  volatile @NotNull Set<AccessRole> roles;
  volatile @NotNull Set<UserGroup> groups;
  volatile @NotNull Set<PhoneNumber> phoneNumbers;
  volatile @NotNull Set<Messenger> messengers;

  volatile int version;
  volatile int passwordVersion;

  volatile boolean emailConfirmed;

  public DefaultUser(
      long id,
      @NotNull Organization organization,
      @NotNull String email,
      byte @NotNull [] password,
      byte @NotNull [] salt,
      @NotNull Set<AccessRole> roles,
      @Nullable String firstName,
      @Nullable String secondName,
      @Nullable String thirdName,
      @Nullable LocalDate birthday,
      @NotNull Set<PhoneNumber> phoneNumbers,
      @NotNull Set<Messenger> messengers,
      @NotNull Instant created,
      @NotNull Instant modified,
      int version,
      int passwordVersion) {
    this.organization = organization;
    this.id = id;
    this.email = email;
    this.password = password;
    this.salt = salt;
    this.roles = roles;
    this.created = created;
    this.modified = modified;
    this.version = version;
    this.firstName = firstName;
    this.secondName = secondName;
    this.thirdName = thirdName;
    this.birthday = birthday;
    this.phoneNumbers = phoneNumbers;
    this.messengers = messengers;
    this.groups = Set.of();
    this.passwordVersion = passwordVersion;
  }
}
