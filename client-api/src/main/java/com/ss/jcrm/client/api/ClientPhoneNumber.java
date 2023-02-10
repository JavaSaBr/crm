package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ClientPhoneNumber {
  @NotNull String countryCode();
  @NotNull String regionCode();
  @NotNull String phoneNumber();
  @NotNull PhoneNumberType type();
}
