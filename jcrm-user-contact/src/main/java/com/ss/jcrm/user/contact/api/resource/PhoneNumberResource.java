package com.ss.jcrm.user.contact.api.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import com.ss.jcrm.user.contact.api.PhoneNumberType;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Value
public class PhoneNumberResource {

    public static @NotNull PhoneNumberResource from(@NotNull PhoneNumber phoneNumber) {
        return new PhoneNumberResource(
            phoneNumber.getCountryCode(),
            phoneNumber.getRegionCode(),
            phoneNumber.getPhoneNumber(),
            (int) phoneNumber.getType().getId()
        );
    }

    @Nullable String countryCode;
    @Nullable String regionCode;
    @Nullable String phoneNumber;

    int type;

    @JsonCreator
    public PhoneNumberResource(
        @JsonProperty("countryCode") @Nullable String countryCode,
        @JsonProperty("regionCode") @Nullable String regionCode,
        @JsonProperty("phoneNumber") @Nullable String phoneNumber,
        @JsonProperty("type") int type
    ) {
        this.countryCode = countryCode;
        this.regionCode = regionCode;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public @NotNull PhoneNumber toPhoneNumber() {
        return new PhoneNumber(
            Objects.requireNonNull(countryCode),
            Objects.requireNonNull(regionCode),
            Objects.requireNonNull(phoneNumber),
            type == 0 ? PhoneNumberType.UNKNOWN : PhoneNumberType.require(type)
        );
    }
}
