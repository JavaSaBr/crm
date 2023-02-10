package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientPhoneNumber;
import com.ss.jcrm.client.api.PhoneNumberType;
import org.jetbrains.annotations.NotNull;

public record DefaultClientPhoneNumber(
    @NotNull String countryCode,
    @NotNull String regionCode,
    @NotNull String phoneNumber,
    @NotNull PhoneNumberType type) implements ClientPhoneNumber {}
