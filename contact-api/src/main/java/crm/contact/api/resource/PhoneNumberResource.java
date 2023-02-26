package crm.contact.api.resource;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.contact.api.PhoneNumber;
import crm.contact.api.PhoneNumberType;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PhoneNumberResource(
    @Nullable String countryCode,
    @Nullable String regionCode,
    @Nullable String phoneNumber,
    long type) {

  public static @NotNull PhoneNumberResource of(
      @NotNull String countryCode,
      @NotNull String regionCode,
      @NotNull String phoneNumber,
      @NotNull PhoneNumberType type) {
    return new PhoneNumberResource(countryCode, regionCode, phoneNumber, type.id());
  }

  public static @NotNull List<PhoneNumberResource> from(@NotNull Collection<PhoneNumber> phoneNumbers) {
    return phoneNumbers
        .stream()
        .map(PhoneNumberResource::from)
        .toList();
  }

  public static @NotNull PhoneNumberResource from(@NotNull PhoneNumber phoneNumber) {
    return new PhoneNumberResource(
        phoneNumber.countryCode(),
        phoneNumber.regionCode(),
        phoneNumber.phoneNumber(),
        phoneNumber.type().id());
  }

  public static @NotNull Set<PhoneNumber> toPhoneNumbers(@Nullable List<PhoneNumberResource> resources) {
    return resources != null ? resources
        .stream()
        .map(PhoneNumberResource::toPhoneNumber)
        .collect(Collectors.toSet()) : Set.of();
  }

  public @NotNull PhoneNumber toPhoneNumber() {
    return PhoneNumber.of(
        notNull(countryCode),
        notNull(regionCode),
        notNull(phoneNumber),
        PhoneNumberType.required(type));
  }
}
