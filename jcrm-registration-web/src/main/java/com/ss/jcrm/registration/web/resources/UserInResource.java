package com.ss.jcrm.registration.web.resources;

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
        @Nullable PhoneNumberResource phoneNumber,
        @Nullable int[] roles,
        @Nullable String birthday
    ) {
        return new UserInResource(email,
            firstName,
            secondName,
            thirdName,
            password,
            phoneNumber,
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

    @Nullable PhoneNumberResource phoneNumber;

    @Nullable int[] roles;
    @Nullable String birthday;

    long id;
}
