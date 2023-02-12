package crm.contact.api;

import crm.contact.api.impl.DefaultPhoneNumber;
import org.jetbrains.annotations.NotNull;

public interface PhoneNumber {
  @NotNull String countryCode();
  @NotNull String regionCode();
  @NotNull String phoneNumber();
  @NotNull PhoneNumberType type();

  static @NotNull PhoneNumber of(
      @NotNull String countryCode,
      @NotNull String regionCode,
      @NotNull String phoneNumber) {
    return of(countryCode, regionCode, phoneNumber, PhoneNumberType.UNKNOWN);
  }

  static @NotNull PhoneNumber of(
      @NotNull String countryCode,
      @NotNull String regionCode,
      @NotNull String phoneNumber,
      @NotNull PhoneNumberType type) {
    return new DefaultPhoneNumber(
        countryCode,
        regionCode,
        phoneNumber,
        countryCode + regionCode + phoneNumber,
        type);
  }
}
