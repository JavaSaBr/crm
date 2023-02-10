package con.ss.jcrm.client.web.resource;

import crm.client.api.ClientPhoneNumber;
import crm.client.api.PhoneNumberType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ClientPhoneNumberResource(
    @Nullable String countryCode,
    @Nullable String regionCode,
    @Nullable String phoneNumber,
    @Nullable String type) {

  public static @NotNull ClientPhoneNumberResource of(
      @NotNull String countryCode,
      @NotNull String regionCode,
      @NotNull String phoneNumber,
      @NotNull PhoneNumberType type) {
    return new ClientPhoneNumberResource(
        countryCode,
        regionCode,
        phoneNumber,
        type.description());
  }

  public static @NotNull ClientPhoneNumberResource from(@NotNull ClientPhoneNumber phoneNumber) {
    return new ClientPhoneNumberResource(
        phoneNumber.countryCode(),
        phoneNumber.regionCode(),
        phoneNumber.phoneNumber(),
        phoneNumber.type().name());
  }
}
