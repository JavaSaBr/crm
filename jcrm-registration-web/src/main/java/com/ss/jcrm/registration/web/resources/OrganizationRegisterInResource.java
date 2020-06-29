package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class OrganizationRegisterInResource implements RestResource {

    @NotNull String orgName;
    @NotNull String email;
    @NotNull String activationCode;
    @NotNull char[] password;

    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;

    @NotNull PhoneNumberResource phoneNumber;

    boolean subscribe;

    long countryId;

    public OrganizationRegisterInResource(
        @NotNull String orgName,
        @NotNull String email,
        @NotNull String activationCode,
        @NotNull char[] password,
        @NotNull PhoneNumberResource phoneNumber,
        long countryId
    ) {
        this.orgName = orgName;
        this.email = email;
        this.activationCode = activationCode;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.countryId = countryId;
        this.firstName = null;
        this.secondName = null;
        this.thirdName = null;
        this.subscribe = false;
    }
}
