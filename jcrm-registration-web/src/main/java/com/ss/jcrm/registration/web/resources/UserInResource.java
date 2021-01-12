package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.jcrm.user.contact.api.resource.MessengerResource;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
public class UserInResource implements RestResource {

    public static @NotNull UserInResource newResource(
        @Nullable String email,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @Nullable char[] password,
        @Nullable PhoneNumberResource[] phoneNumbers,
        @Nullable MessengerResource[] messengers,
        @Nullable int[] roles,
        @Nullable String birthday
    ) {
        return new UserInResource(email,
            firstName,
            secondName,
            thirdName,
            password,
            phoneNumbers,
            messengers,
            roles,
            birthday,
            0
        );
    }

    @Nullable String email;
    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;

    @Nullable char[] password;

    @Nullable PhoneNumberResource[] phoneNumbers;
    @Nullable MessengerResource[] messengers;

    @Nullable int[] roles;

    @Nullable String birthday;

    long id;

    @JsonCreator
    public UserInResource(
        @JsonProperty("email") @Nullable String email,
        @JsonProperty("firstName") @Nullable String firstName,
        @JsonProperty("secondName") @Nullable String secondName,
        @JsonProperty("thirdName") @Nullable String thirdName,
        @JsonProperty("password") @Nullable char[] password,
        @JsonProperty("phoneNumbers") @Nullable PhoneNumberResource[] phoneNumbers,
        @JsonProperty("messengers") @Nullable MessengerResource[] messengers,
        @JsonProperty("roles") @Nullable int[] roles,
        @JsonProperty("birthday") @Nullable String birthday,
        @JsonProperty("id") long id
    ) {
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.password = password;
        this.phoneNumbers = phoneNumbers;
        this.messengers = messengers;
        this.roles = roles;
        this.birthday = birthday;
        this.id = id;
    }
}
