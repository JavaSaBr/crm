package crm.client.api.impl;

import crm.client.api.ClientPhoneNumber;
import crm.client.api.PhoneNumberType;
import org.jetbrains.annotations.NotNull;

public record DefaultClientPhoneNumber(
    @NotNull String countryCode,
    @NotNull String regionCode,
    @NotNull String phoneNumber,
    @NotNull PhoneNumberType type) implements ClientPhoneNumber {}
