package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.user.contact.api.PhoneNumber;
import com.ss.jcrm.user.contact.api.PhoneNumberType;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import org.jetbrains.annotations.Nullable;

public abstract class BaseRegistrationHandler {

    protected static @Nullable PhoneNumber toPhoneNumber(@Nullable PhoneNumberResource resource) {

        if (resource == null) {
            return null;
        }

        return new PhoneNumber(
            resource.getCountryCode(),
            resource.getRegionCode(),
            resource.getPhoneNumber(),
            PhoneNumberType.of(resource.getType(), PhoneNumberType.UNKNOWN)
        );
    }

}
