package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.contact.api.resource.MessengerResource;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
public record UserOutResource(
    @NotNull String email,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable String birthday,
    PhoneNumberResource @Nullable [] phoneNumbers,
    MessengerResource @Nullable [] messengers,
    long @NotNull [] roles,
    long id,
    long created,
    long modified
) implements RestResource {

    public static @NotNull UserOutResource from(@NotNull User user) {

        return new UserOutResource(
            user.getEmail(),
            user.getFirstName(),
            user.getSecondName(),
            user.getThirdName(),
            DateUtils.toString(user.getBirthday()),
            user.getPhoneNumbers()
                .stream()
                .map(PhoneNumberResource::from)
                .toArray(PhoneNumberResource[]::new),
            user.getMessengers()
                .stream()
                .map(MessengerResource::from)
                .toArray(MessengerResource[]::new),
            user.getRoles()
                .stream()
                .mapToLong(AccessRole::getId)
                .toArray(),
            user.getId(),
            user.getCreated().toEpochMilli(),
            user.getModified().toEpochMilli()
        );
    }
}
