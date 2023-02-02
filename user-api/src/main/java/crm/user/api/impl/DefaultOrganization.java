package crm.user.api.impl;

import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.Industry;
import crm.user.api.Organization;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Accessors(fluent = true, chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultOrganization implements Organization {

  final long id;

  volatile int version;

  @NotNull String name;
  @Nullable String zipCode;
  @Nullable String address;
  @Nullable String email;
  @Nullable String phoneNumber;

  @Nullable City city;
  @Nullable Country country;
  @NotNull Set<Industry> industries;

  public DefaultOrganization(long id, int version, @NotNull String name) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.industries = Set.of();
  }

  public DefaultOrganization(long id, int version, @NotNull String name, @NotNull Country country) {
    this(id, version, name);
    this.country = country;
  }
}
