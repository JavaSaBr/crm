package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@Value
public class OrganizationRegisterInResource implements RestResource {

    @Nullable String orgName;
    @Nullable String email;
    @Nullable String activationCode;
    @Nullable char[] password;

    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;

    @Nullable PhoneNumberResource phoneNumber;

    boolean subscribe;

    long countryId;

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public OrganizationRegisterInResource(
        @Nullable String orgName,
        @Nullable String email,
        @Nullable String activationCode,
        @Nullable char[] password,
        @Nullable PhoneNumberResource phoneNumber,
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

    @JsonCreator
    public OrganizationRegisterInResource(
        @JsonProperty("orgName") @Nullable String orgName,
        @JsonProperty("email") @Nullable String email,
        @JsonProperty("activationCode") @Nullable String activationCode,
        @JsonProperty("password") @Nullable char[] password,
        @JsonProperty("firstName") @Nullable String firstName,
        @JsonProperty("secondName") @Nullable String secondName,
        @JsonProperty("thirdName") @Nullable String thirdName,
        @JsonProperty("phoneNumber") @Nullable PhoneNumberResource phoneNumber,
        @JsonProperty("subscribe") boolean subscribe,
        @JsonProperty("countryId") long countryId
    ) {
        this.orgName = orgName;
        this.email = email;
        this.activationCode = activationCode;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.phoneNumber = phoneNumber;
        this.subscribe = subscribe;
        this.countryId = countryId;
    }
}
