package com.ss.jcrm.user.contact.api.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class PhoneNumberResource {

    @NotNull String countryCode;
    @NotNull String regionCode;
    @NotNull String phoneNumber;

    @Nullable String type;

    public PhoneNumberResource(@NotNull PhoneNumber phoneNumber) {
        this.countryCode = phoneNumber.getCountryCode();
        this.regionCode = phoneNumber.getRegionCode();
        this.phoneNumber = phoneNumber.getPhoneNumber();
        this.type = phoneNumber.getType().name();
    }
}
