package com.ss.jcrm.user.contact.api.resource;

import com.ss.jcrm.user.contact.api.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberResource {

    private String countryCode;
    private String regionCode;
    private String phoneNumber;
    private String type;

    public PhoneNumberResource(@NotNull PhoneNumber phoneNumber) {
        this.countryCode = phoneNumber.getCountryCode();
        this.regionCode = phoneNumber.getRegionCode();
        this.phoneNumber = phoneNumber.getPhoneNumber();
        this.type = phoneNumber.getType().name();
    }
}
