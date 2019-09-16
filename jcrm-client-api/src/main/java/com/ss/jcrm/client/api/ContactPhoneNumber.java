package com.ss.jcrm.client.api;

import org.jetbrains.annotations.NotNull;

public interface ContactPhoneNumber {

    @NotNull String getCountryCode();

    void setCountryCode(@NotNull String countryCode);

    @NotNull String getRegionCode();

    void setRegionCode(@NotNull String regionCode);

    @NotNull String getPhoneNumber();

    void setPhoneNumber(@NotNull String phoneNumber);

    @NotNull PhoneNumberType getType();

    void setType(@NotNull PhoneNumberType type);
}
