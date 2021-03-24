package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientPhoneNumber;
import com.ss.jcrm.client.api.PhoneNumberType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultClientPhoneNumber implements ClientPhoneNumber {

    private String countryCode;
    private String regionCode;
    private String phoneNumber;
    private PhoneNumberType type;
}
