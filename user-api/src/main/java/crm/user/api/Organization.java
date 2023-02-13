package crm.user.api;

import crm.dao.NamedUniqEntity;
import crm.dao.VersionedUniqEntity;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.Industry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Organization extends NamedUniqEntity, VersionedUniqEntity {

  @Nullable String address();
  Organization address(@Nullable String address);

  @Nullable String zipCode();
  Organization zipCode(@Nullable String zipCode);

  @Nullable String email();
  Organization email(@Nullable String email);

  @Nullable String phoneNumber();
  Organization phoneNumber(@Nullable String phoneNumber);

  @Nullable City city();
  Organization city(@Nullable City city);

  @NotNull Country country();
  Organization country(@NotNull Country country);

  @NotNull Set<Industry> industries();
  Organization industries(@NotNull Set<Industry> industries);
}
