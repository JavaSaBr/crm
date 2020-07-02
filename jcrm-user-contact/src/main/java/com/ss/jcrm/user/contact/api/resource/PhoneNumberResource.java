package com.ss.jcrm.user.contact.api.resource;

import com.ss.jcrm.user.contact.api.PhoneNumber;
import com.ss.jcrm.user.contact.api.PhoneNumberType;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

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

    @NotNull String countryCode;
    @NotNull String regionCode;
    @NotNull String phoneNumber;

    int type;

    public @NotNull PhoneNumber toPhoneNumber() {
        return new PhoneNumber(
            countryCode,
            regionCode,
            phoneNumber,
            type == 0 ? PhoneNumberType.UNKNOWN : PhoneNumberType.require(type)
        );
    }
}
