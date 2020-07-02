package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.contact.api.resource.MessengerResource;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinimalUserOutResource implements RestResource {

    @NotNull String email;

    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;
    @Nullable String birthday;

    @Nullable PhoneNumberResource[] phoneNumbers;
    @Nullable MessengerResource[] messengers;

    long id;

    public MinimalUserOutResource(@NotNull User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
        this.thirdName = user.getThirdName();
        this.phoneNumbers = user.getPhoneNumbers()
            .stream()
            .map(PhoneNumberResource::from)
            .toArray(PhoneNumberResource[]::new);
        this.messengers = user.getMessengers()
            .stream()
            .map(MessengerResource::from)
            .toArray(MessengerResource[]::new);
        this.birthday = DateUtils.toString(user.getBirthday());
        this.id = user.getId();
    }
}
