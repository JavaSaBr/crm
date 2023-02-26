package crm.user.api.impl;

import crm.security.AccessRole;
import crm.user.api.Organization;
import crm.user.api.UserGroup;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@ToString(of = {
    "id",
    "name",
    "organization"
})
@EqualsAndHashCode(of = "id")
@Accessors(fluent = true, chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultUserGroup implements UserGroup {

  final long id;
  final @NotNull Instant created;
  final @NotNull Organization organization;

  volatile @NotNull String name;
  volatile @NotNull Set<AccessRole> roles;
  volatile @NotNull Instant modified;

  private volatile int version;

  public DefaultUserGroup(
      long id,
      @NotNull String name,
      @NotNull Set<AccessRole> roles,
      @NotNull Organization organization,
      @NotNull Instant created,
      @NotNull Instant modified,
      int version) {
    this.id = id;
    this.created = created;
    this.organization = organization;
    this.name = name;
    this.roles = roles;
    this.modified = modified;
    this.version = version;
  }
}
