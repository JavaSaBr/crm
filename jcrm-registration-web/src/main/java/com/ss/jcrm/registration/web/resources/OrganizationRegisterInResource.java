package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record OrganizationRegisterInResource(
    @Nullable String orgName,
    @Nullable String email,
    @Nullable String activationCode,
    char @Nullable [] password,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable PhoneNumberResource phoneNumber,
    boolean subscribe,
    long countryId
) implements RestResource {

    public static @NotNull OrganizationRegisterInResource from(
        @Nullable String orgName,
        @Nullable String email,
        @Nullable String activationCode,
        char @Nullable [] password,
        @Nullable PhoneNumberResource phoneNumber,
        long countryId
    ) {
        return new OrganizationRegisterInResource(
            orgName,
            email,
            activationCode,
            password,
            null,
            null,
            null,
            phoneNumber,
            false,
            countryId
        );
    }
}
