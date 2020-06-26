package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInResource implements RestResource {

    @Nullable String email;
    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;
    @Nullable PhoneNumberResource phoneNumber;

    long id;

    private UserInResource(
        @JsonProperty("email") @Nullable String email,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @Nullable PhoneNumberResource phoneNumber,
        long id
    ) {
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }
}
