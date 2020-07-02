package com.ss.jcrm.registration.web.handler;

import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import com.ss.jcrm.user.contact.api.resource.MessengerResource;
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseRegistrationHandler {

    protected static @NotNull Set<PhoneNumber> toPhoneNumbers(@Nullable PhoneNumberResource[] resources) {

        if (resources == null || resources.length == 0) {
            return Set.of();
        }

        return Stream.of(resources)
            .filter(Objects::nonNull)
            .map(PhoneNumberResource::toPhoneNumber)
            .collect(Collectors.toSet());
    }

    protected static @NotNull Set<Messenger> toMessengers(@Nullable MessengerResource[] resources) {

        if (resources == null || resources.length == 0) {
            return Set.of();
        }

        return Stream.of(resources)
            .filter(Objects::nonNull)
            .map(MessengerResource::toMessenger)
            .collect(Collectors.toSet());
    }
}
