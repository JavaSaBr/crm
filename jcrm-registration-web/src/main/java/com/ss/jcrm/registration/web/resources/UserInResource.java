package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.contact.api.resource.MessengerResource;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UserInResource(
    @Nullable String email,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    char @Nullable [] password,
    @Nullable PhoneNumberResource[] phoneNumbers,
    @Nullable MessengerResource[] messengers,
    int @Nullable [] roles,
    @Nullable String birthday,
    long id
) implements RestResource {

    public static @NotNull UserInResource from(
        @Nullable String email,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        char @Nullable [] password,
        @Nullable PhoneNumberResource[] phoneNumbers,
        @Nullable MessengerResource[] messengers,
        int @Nullable [] roles,
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
}
