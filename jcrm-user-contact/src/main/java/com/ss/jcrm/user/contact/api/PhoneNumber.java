package com.ss.jcrm.user.contact.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@RequiredArgsConstructor(onConstructor_ = @JsonCreator)
public class PhoneNumber {

    private static final @NotNull PhoneNumber[] EMPTY_ARRAY = new PhoneNumber[0];

    @NotNull String countryCode;
    @NotNull String regionCode;
    @NotNull String phoneNumber;

    @NotNull String fullPhoneNumber;

    @NotNull PhoneNumberType type;

    public PhoneNumber(
        @NotNull String countryCode,
        @NotNull String regionCode,
        @NotNull String phoneNumber
    ) {
        this(countryCode, regionCode, phoneNumber, PhoneNumberType.UNKNOWN);
    }

    public PhoneNumber(
        @NotNull String countryCode,
        @NotNull String regionCode,
        @NotNull String phoneNumber,
        @NotNull PhoneNumberType type
    ) {
        this.countryCode = countryCode;
        this.regionCode = regionCode;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.fullPhoneNumber = countryCode + regionCode + phoneNumber;
    }
}
