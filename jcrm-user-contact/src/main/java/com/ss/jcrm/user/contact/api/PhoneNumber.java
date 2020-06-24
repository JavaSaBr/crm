package com.ss.jcrm.user.contact.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class PhoneNumber {

    private static final @NotNull PhoneNumber[] EMPTY_ARRAY = new PhoneNumber[0];

    @NotNull String countryCode;
    @NotNull String regionCode;
    @NotNull String phoneNumber;

    @NotNull PhoneNumberType type;

    public PhoneNumber(
        @NotNull String countryCode,
        @NotNull String regionCode,
        @NotNull String phoneNumber
    ) {
        this(countryCode, regionCode, phoneNumber, PhoneNumberType.UNKNOWN);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PhoneNumber(
        @JsonProperty("countryCode") @NotNull String countryCode,
        @JsonProperty("regionCode") @NotNull String regionCode,
        @JsonProperty("phoneNumber") @NotNull String phoneNumber,
        @JsonProperty("type") @NotNull PhoneNumberType type
    ) {
        this.countryCode = countryCode;
        this.regionCode = regionCode;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    @JsonIgnore
    public @NotNull String plainView() {
        return countryCode + regionCode + phoneNumber;
    }
}
