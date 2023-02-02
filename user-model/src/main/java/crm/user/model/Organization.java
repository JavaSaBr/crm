package crm.user.model;

import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.Industry;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Organization extends NamedUniqEntity, VersionedUniqEntity {

    @Nullable String getAddress();

    void setAddress(@Nullable String address);

    @Nullable String getZipCode();

    void setZipCode(@Nullable String zipCode);

    @Nullable String getEmail();

    void setEmail(@Nullable String email);

    @Nullable String getPhoneNumber();

    void setPhoneNumber(@Nullable String phoneNumber);

    @Nullable City getCity();

    void setCity(@Nullable City city);

    @NotNull Country getCountry();

    void setCountry(@NotNull Country country);

    @NotNull Set<Industry> getIndustries();

    void setIndustries(@NotNull Set<Industry> industries);
}
