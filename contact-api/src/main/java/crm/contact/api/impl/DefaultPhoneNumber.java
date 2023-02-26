package crm.contact.api.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import crm.contact.api.PhoneNumber;
import crm.contact.api.PhoneNumberType;
import org.jetbrains.annotations.NotNull;

public record DefaultPhoneNumber(
    @NotNull String countryCode,
    @NotNull String regionCode,
    @NotNull String phoneNumber,
    @NotNull String fullPhoneNumber,
    @NotNull PhoneNumberType type) implements PhoneNumber {}
