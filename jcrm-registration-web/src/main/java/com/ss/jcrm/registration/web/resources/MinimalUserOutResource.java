package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
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
    @Nullable PhoneNumberResource phoneNumber;

    long id;

    public MinimalUserOutResource(@NotNull User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
        this.thirdName = user.getThirdName();
        this.phoneNumber = user.getPhoneNumber() == null ? null : new PhoneNumberResource(user.getPhoneNumber());
        this.id = user.getId();
    }
}
