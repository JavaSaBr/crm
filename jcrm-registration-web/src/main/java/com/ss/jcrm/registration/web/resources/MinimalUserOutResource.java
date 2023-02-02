package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import crm.user.api.MinimalUser;
import crm.user.api.User;
import com.ss.jcrm.user.contact.api.resource.MessengerResource;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
public record MinimalUserOutResource(
    @NotNull String email,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable String birthday,
    PhoneNumberResource @Nullable [] phoneNumbers,
    MessengerResource @Nullable [] messengers,
    long id
) implements RestResource {

    public static @NotNull MinimalUserOutResource from(@NotNull MinimalUser user) {
        return new MinimalUserOutResource(
            user.email(),
            null,
            null,
            null,
            null,
            null,
            null,
            user.id()
        );
    }

    public static @NotNull MinimalUserOutResource from(@NotNull User user) {
        return new MinimalUserOutResource(
            user.email(),
            user.firstName(),
            user.secondName(),
            user.thirdName(),
            DateUtils.toString(user.birthday()),
            user.phoneNumbers()
                .stream()
                .map(PhoneNumberResource::from)
                .toArray(PhoneNumberResource[]::new),
            user.messengers()
                .stream()
                .map(MessengerResource::from)
                .toArray(MessengerResource[]::new),
            user.id()
        );
    }
}
