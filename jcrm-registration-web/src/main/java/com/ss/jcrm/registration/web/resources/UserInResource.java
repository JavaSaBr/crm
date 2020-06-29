package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import com.ss.jcrm.web.resources.RestResource;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

@Value
public class UserInResource implements RestResource {

    @Nullable String email;
    @Nullable String firstName;
    @Nullable String secondName;
    @Nullable String thirdName;
    @Nullable PhoneNumberResource phoneNumber;

    long id;
}
